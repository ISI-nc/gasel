package gasel.maintenance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.dba.db2.DB2PkGenerator;
import org.apache.cayenne.map.DataMap;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.DbEntity;

import tests.model.AllModelTests;

public class CorrectionDesSequences {

	public static void main(String[] args) {
		new CorrectionDesSequences().run();
	}

	private Configuration config;

	private DataDomain domain;

	private DataMap map;

	private DbEntity entity;

	private PkGenerator pkGenerator;

	private DbAttribute id;

	private DataNode node;

	public CorrectionDesSequences() {
		pkGenerator = new PkGenerator();
	}

	@SuppressWarnings("deprecation")
	public void run() {
		AllModelTests.setupDatabase("noumea");

		config = Configuration.getSharedConfiguration();
		domain = config.getDomain();
		node = (DataNode) domain.getDataNodes().iterator().next();
		for (Object o : domain.getDataMaps()) {
			this.map = (DataMap) o;
			traiteMap();
		}
	}

	private void traiteMap() {
		for (Object o : map.getDbEntities()) {
			entity = (DbEntity) o;
			traiteEntity();
		}
	}

	@SuppressWarnings("deprecation")
	private void traiteEntity() {
		// On estime une séquence max par table
		if (entity.getPrimaryKeyGenerator() != null) {
			System.out.println(entity.getPrimaryKeyGenerator()
					.getGeneratorName());
		}

		if (entity.getPrimaryKey().size() != 1) {
			// System.out.println("(ii) " + entity.getName() + " skipped: PK sur
			// "
			// + entity.getPrimaryKey().size() + " champs.");
			return;
		}

		id = (DbAttribute) entity.getPrimaryKey().get(0);
		if (!id.isGenerated()) {
			// System.out.println("(ii) " + entity.getName()
			// + " skipped: PK non générée.");
			return;
		}

		try {
			traiteSequence();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void traiteSequence() throws SQLException {
		String seqName = pkGenerator.sequenceName(entity);
		System.out.println("(ii) " + entity.getName() + ": sequence: "
				+ seqName);

		// Suppresison de la séquence
		pkGenerator.runUpdate(node, "DROP SEQUENCE " + seqName + " RESTRICT");

		// Recalcul du prochain numéro
		Connection conn = node.getDataSource().getConnection();
		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery("select max(" + id.getName() + ") from "
				+ entity.getFullyQualifiedName());
		rs.next();
		long maxId = rs.getLong(1);
		conn.close();

		System.out.println("`-> max ID: " + maxId);

		long nextId = maxId + pkGenerator.getPkCacheSize() //
				- maxId % pkGenerator.getPkCacheSize();
		if (nextId < 200)
			nextId = 200;
		System.out.println("`-> next ID: " + nextId);

		// Création de la séquence au bon état
		pkGenerator.runUpdate(node, "CREATE SEQUENCE " + seqName
				+ " START WITH " + nextId + " INCREMENT BY "
				+ pkGenerator.getPkCacheSize() + " NO MAXVALUE" + " NO CYCLE"
				+ " CACHE " + pkGenerator.getPkCacheSize());
	}

	@SuppressWarnings("deprecation")
	protected static class PkGenerator extends DB2PkGenerator {
		@Override
		protected String sequenceName(DbEntity entity) {
			return super.sequenceName(entity);
		}
		
//		@Override
//		public int runUpdate(DataNode node, String sql) throws SQLException {
//			// noop
//			return 0;
//		}
	}

}
