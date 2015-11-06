package gasel.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import nc.ccas.gasel.model.core.Personne;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.DbEntity;
import org.apache.cayenne.map.DbRelationship;
import org.apache.cayenne.map.ObjAttribute;

import tests.model.AllModelTests;

public class CreateIndexes {

	public static void main(String[] args) throws SQLException {
		AllModelTests.setupDatabase("current");

		CreateIndexes indexCreator = new CreateIndexes();

		indexCreator.createJoinIndexes();

		indexCreator.indexProperties(Personne.class, Personne.NOM_PROPERTY,
				Personne.NOM_USAGE_PROPERTY, Personne.NOM_JEUNE_FILLE_PROPERTY);
		indexCreator.indexProperties(Personne.class, Personne.PRENOM_PROPERTY,
				Personne.PRENOM_USAGE_PROPERTY);
	}

	private DataDomain domain;

	private Connection conn;

	public CreateIndexes() throws SQLException {
		this(Configuration.getSharedConfiguration());
	}

	public CreateIndexes(Configuration config) throws SQLException {
		this(config.getDomain());
	}

	public CreateIndexes(DataDomain domain) throws SQLException {
		this.domain = domain;
		conn = ((DataNode) domain.getDataNodes().iterator().next())
				.getDataSource().getConnection();
	}

	public void createJoinIndexes() throws SQLException {
		for (DbEntity dbEnt : checkedIterable(DbEntity.class, domain
				.getEntityResolver().getDbEntities())) {
			if (MigrateDb.MAPS_TO_SKIP.contains(dbEnt.getDataMap().getName())) {
				continue;
			}
			for (DbRelationship rel : checkedIterable(DbRelationship.class,
					dbEnt.getRelationships())) {
				for (DbAttribute attr : checkedIterable(DbAttribute.class,
						rel.getSourceAttributes())) {
					if (attr.isPrimaryKey()) {
						continue;
					}
					indexAttribute(attr);
				}
			}
		}
	}

	public void indexProperties(Class<? extends DataObject> entityClass,
			String... propertyNames) throws SQLException {
		for (String propertyName : propertyNames) {
			ObjAttribute objAttr = (ObjAttribute) domain.getEntityResolver()
					.lookupObjEntity(entityClass).getAttribute(propertyName);
			DbAttribute dbAttr = objAttr.getDbAttribute();
			indexAttribute(dbAttr);
		}
	}

	private void indexAttribute(DbAttribute attr) throws SQLException {
		DbEntity ent = (DbEntity) attr.getEntity();
		String sql = "CREATE INDEX " + ent.getName() + "__" + attr.getName()
				+ "__idx ON " + ent.getFullyQualifiedName() + "("
				+ attr.getName() + ")";
		System.out.println(sql);
		PreparedStatement stmt = conn.prepareStatement(sql);
		try {
			stmt.execute();
		} catch (SQLException e) {
			if (e.getMessage().matches("ERROR: relation .* already exists")) {
				System.out.println("`-> " + e.getMessage());
			}
		}
	}

	public <T> Iterable<T> checkedIterable(final Class<T> clazz,
			final Iterable<?> collection) {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return new Iterator<T>() {
					Iterator<?> delegate = collection.iterator();

					@Override
					public boolean hasNext() {
						return delegate.hasNext();
					}

					@Override
					public T next() {
						return clazz.cast(delegate.next());
					}

					@Override
					public void remove() {
						delegate.remove();
					}
				};
			}
		};
	}

}
