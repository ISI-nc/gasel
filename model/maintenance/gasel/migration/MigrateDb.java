package gasel.migration;

import static com.asystan.common.StringUtils.join;
import static java.util.Collections.synchronizedCollection;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sql.DataSource;

import org.apache.cayenne.CayenneException;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.conf.DBCPDataSourceFactory;
import org.apache.cayenne.dba.AutoAdapter;
import org.apache.cayenne.dba.DbAdapter;
import org.apache.cayenne.dba.PkGenerator;
import org.apache.cayenne.map.DataMap;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.DbEntity;
import org.apache.cayenne.map.DbRelationship;
import org.apache.cayenne.query.SelectQuery;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.asystan.common.lists.ListUtils;

public class MigrateDb {

	public static final int INSERT_BATCH_SIZE = 1000;

	public static final Set<String> MAPS_TO_SKIP = new TreeSet<String>(
			Arrays.asList("_gasel", "_import_v1", "_budget"));

	private static final Logger LOG = Logger.getLogger(MigrateDb.class);

	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure(new File("log4j-migration.properties")
				.toURI().toURL());

		MigrateDb migrateDb = new MigrateDb(new MapTransformation() {
			@Override
			protected String translateName(DbEntity srcEntity) {
				if (!Objects.equals("gasel_v2", srcEntity.getSchema())) {
					return srcEntity.getSchema() + "__" + srcEntity.getName();
				}
				return srcEntity.getName();
			}
		});

		// setup
		migrateDb.getMapsToSkip().addAll(MAPS_TO_SKIP);
		migrateDb.setWorkerCount(4);

		migrateDb.setupFinished();

		// check we can read all the tables in the source
		migrateDb.checkSourceTables();

		// build the target
		migrateDb.resetTarget();
		migrateDb.migrateData();
		migrateDb.createFKConstraints();

		LOG.info("Finished.");
	}

	private final Set<String> mapsToSkip = new TreeSet<String>();

	private final DataDomain domain;
	private List<DataMap> maps;
	private List<DbEntity> entities;

	private String source;
	private String target;

	private int workerCount = 1;

	private DataNode _sourceNode;

	private DataNode _targetNode;

	private final MapTransformation transformation;

	private final Configuration configuration;

	public MigrateDb(MapTransformation transformation) {
		this(Configuration.getSharedConfiguration(), transformation);
	}

	public MigrateDb(Configuration configuration,
			MapTransformation transformation) {
		this.configuration = configuration;
		this.transformation = transformation;
		this.domain = configuration.getDomain();
	}

	public void setupFinished() {
		transformation.setup(maps());
	}

	public void resetTarget() throws Exception {
		DbAdapter adapter = targetNode().getAdapter();
		PkGenerator pkGen = adapter.getPkGenerator();

		List<DbEntity> entities = targetEntities();

		Connection target = targetNode().getDataSource().getConnection();
		try {
			LOG.info("Dropping PK support in target...");
			for (Object o : pkGen.dropAutoPkStatements(entities)) {
				String sql = (String) o;
				safeSql(sql, target);
			}

			LOG.info("Dropping tables in target...");
			for (DbEntity entity : entities) {
				safeSql(adapter.dropTable(entity), target);
			}

			LOG.info("Creating tables in target...");
			for (DbEntity entity : entities) {
				safeSql(adapter.createTable(entity), target);
			}

			LOG.info("Creating PK support in target...");
			for (Object o : pkGen.createAutoPkStatements(entities)) {
				String sql = (String) o;
				safeSql(sql, target);
			}
		} finally {
			safeClose(target);
		}
	}

	private List<DbEntity> targetEntities() {
		DataMap map = transformation.getMap();
		return ListUtils.cast(DbEntity.class, map.getDbEntities());
	}

	/**
	 * Execute SQL code, ignoring errors.
	 */
	private void safeSql(String sql, Connection connection) {
		try {
			connection.createStatement().execute(sql);
		} catch (SQLException e) {
			LOG.debug("Failed SQL: " + sql);
			LOG.debug(e);
		} finally {
			try {
				connection.commit();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void createFKConstraints() throws Exception {
		if (true) {
			LOG.warn("Not creating FKs as it seems broken (please use provided SQL script).");
			return;
		}
		DbAdapter adapter = targetNode().getAdapter();
		LOG.info("Creating FK constraints in target...");
		Connection target = targetNode().getDataSource().getConnection();
		try {
			Statement stmt = target.createStatement();
			for (DbEntity entity : targetEntities()) {
				for (DbRelationship rel : ListUtils.cast(DbRelationship.class,
						entity.getRelationships())) {
					if (rel.isToMany()) {
						continue; // skip reverse relations
					}
					String sql = adapter.createFkConstraint(rel);
					try {
						stmt.execute(sql);
					} catch (SQLException e) {
						LOG.warn("Error creating FK constraint: " + sql);
						LOG.warn(e);
					} finally {
						target.commit();
					}
				}
			}
			stmt.close();
		} finally {
			safeClose(target);
		}
	}

	public void migrateData() throws CayenneException {
		final List<Exception> failures = new LinkedList<Exception>();

		List<DbEntity> entities = entities();
		LOG.info("Migrating " + entities.size() + " entities...");

		LOG.info("- Launching data migration workers...");
		processEntities(entities, new DbEntityProcessor() {
			@Override
			public void process(DbEntity entity) {
				DbEntity targetEntity = transformation.target(entity);

				Connection source = null;
				Connection target = null;

				try {
					source = sourceNode().getDataSource().getConnection();
					target = targetNode().getDataSource().getConnection();

					PreparedStatement selectSt = source
							.prepareStatement(selectSql(entity));
					ResultSet selectResults = selectSt.executeQuery();
					int columnCount = selectResults.getMetaData()
							.getColumnCount();

					PreparedStatement insertSt = target
							.prepareStatement(insertSql(targetEntity));

					int batchSize = 0;
					while (selectResults.next()) {
						for (int col = 1; col <= columnCount; col++) {
							Object value = selectResults.getObject(col);
							insertSt.setObject(col, value);
						}
						insertSt.addBatch();
						batchSize += 1;
						if (batchSize >= INSERT_BATCH_SIZE) {
							execBatch(target, insertSt);
							batchSize = 0;
						}
					}
					if (batchSize > 0) {
						execBatch(target, insertSt);
					}

					selectResults.close();
					insertSt.close();
				} catch (Exception e) {
					LOG.error(e);
					failures.add(e);
				} finally {
					safeClose(source);
					safeClose(target);
				}
			}

			private void execBatch(Connection target, PreparedStatement insertSt)
					throws SQLException {
				try {
					insertSt.executeBatch();
				} catch (SQLException e) {
					throw e.getNextException();
				}
				target.commit();
				// insertSt.clearBatch();
			}

			private String selectSql(DbEntity entity) {
				return "select " + join(", ", attributesOf(entity)) + " from "
						+ entity.getFullyQualifiedName();
			}

			private String insertSql(DbEntity entity) {
				SortedSet<String> attrs = attributesOf(entity);

				StringBuilder buf = new StringBuilder("insert into ");
				buf.append(entity.getFullyQualifiedName());
				buf.append(" (");
				buf.append(join(", ", attrs)).append(") VALUES (");

				boolean first = true;
				for (@SuppressWarnings("unused")
				String attribute : attributesOf(entity)) {
					if (first) {
						first = false;
					} else {
						buf.append(", ");
					}
					buf.append("?");
				}
				buf.append(")");
				return buf.toString();
			}

			private SortedSet<String> attributesOf(DbEntity entity) {
				SortedSet<String> attributes = new TreeSet<>();
				for (Object o : entity.getAttributes()) {
					DbAttribute attr = (DbAttribute) o;
					attributes.add(attr.getName());
				}
				return attributes;
			}

		});

		if (!failures.isEmpty()) {
			throw new RuntimeException("Failures during migration (see logs)");
		}
	}

	private void processEntities(final DbEntityProcessor processor) {
		processEntities(entities(), processor);
	}

	private void processEntities(final Collection<DbEntity> entitiesToProcess,
			final DbEntityProcessor processor) {
		final Collection<DbEntity> processed = synchronizedCollection(new Stack<DbEntity>());
		final BlockingQueue<DbEntity> entities = new LinkedBlockingQueue<DbEntity>();
		entities.addAll(entitiesToProcess);
		Thread[] workers = new Thread[workerCount];
		for (int idx = 0; idx < workerCount; idx++) {
			workers[idx] = new Thread() {
				public void run() {
					DbEntity entity;
					while ((entity = entities.poll()) != null) {
						try {
							processor.process(entity);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}

						processed.add(entity);
						LOG.info("Task progress: " + processed.size() + "/"
								+ entitiesToProcess.size());
					}
				}
			};
			workers[idx].start();
		}

		for (Thread worker : workers) {
			try {
				worker.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static interface DbEntityProcessor {
		public void process(DbEntity entity) throws Exception;
	}

	public void checkSourceTables() throws CayenneException {
		// Try to fetch one row of each entity
		LOG.info("Checking source tables...");
		final Set<String> failedEntities = new TreeSet<String>();
		final DataContext dc = DataContext.createDataContext();
		processEntities(new DbEntityProcessor() {
			@Override
			public void process(DbEntity entity) {
				SelectQuery select = new SelectQuery(entity);
				select.setFetchingDataRows(true);
				select.setFetchLimit(1);
				try {
					dc.performIteratedQuery(select);
				} catch (CayenneException e) {
					failedEntities.add(entity.getName());
				}
			}
		});
		// failed?
		if (!failedEntities.isEmpty()) {
			throw new CayenneException("Failed to fetch "
					+ failedEntities.size()
					+ (failedEntities.size() > 1 ? " entities: " : " entity: ")
					+ failedEntities);
		}
	}

	private List<DataMap> maps() {
		if (maps == null) {
			maps = new ArrayList<DataMap>();

			for (Object o : domain.getDataMaps()) {
				DataMap dataMap = (DataMap) o;
				if (mapsToSkip.contains(dataMap.getName())) {
					continue;
				}
				maps.add(dataMap);
			}
		}
		return maps;
	}

	@SuppressWarnings("unchecked")
	private List<DbEntity> entities() {
		if (entities == null) {
			entities = new ArrayList<DbEntity>();
			for (DataMap map : maps()) {
				entities.addAll(map.getDbEntities());
			}
		}
		return entities;
	}

	private DataNode sourceNode() {
		if (_sourceNode == null) {
			_sourceNode = createDataNode("source");
		}
		return _sourceNode;
	}

	private DataNode targetNode() {
		if (_targetNode == null) {
			_targetNode = createDataNode("target");
			DbAdapter adapter = new TargetDbAdapter(_targetNode.getAdapter(),
					transformation);
			_targetNode.setAdapter(adapter);
		}
		return _targetNode;
	}

	private DataNode createDataNode(String dbName) {
		DataNode node = new DataNode(dbName);

		DBCPDataSourceFactory dataSourceFactory = new DBCPDataSourceFactory();
		dataSourceFactory.initializeWithParentConfiguration(configuration);

		DataSource dataSource;
		try {
			dataSource = dataSourceFactory.getDataSource("migration-" + dbName
					+ ".properties");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		node.setDataSource(dataSource);
		node.setEntityResolver(domain.getEntityResolver());
		node.setAdapter(new AutoAdapter(dataSource));
		return node;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String destination) {
		this.target = destination;
	}

	public Set<String> getMapsToSkip() {
		return mapsToSkip;
	}

	public int getWorkerCount() {
		return workerCount;
	}

	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}

	private void safeClose(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
