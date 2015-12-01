package dia_modeller.ext;

import org.apache.cayenne.Persistent;
import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.map.DbEntity;
import org.apache.cayenne.map.DbJoin;
import org.apache.cayenne.map.DbRelationship;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;

import cayennetools.dia_modeller.NameTranslator;
import cayennetools.dia_modeller.impl.NameTranslatorImpl;

public class AlterationUtils {

	private static final NameTranslator nameTranslator = new NameTranslatorImpl();

	public static void addToOne(DataDomain domain,
			Class<? extends Persistent> from, Class<? extends Persistent> to,
			String relName, String... joins) {
		ObjEntity fromEnt = domain.getEntityResolver().lookupObjEntity(from);
		ObjEntity toEnt = domain.getEntityResolver().lookupObjEntity(to);
		addToOne(fromEnt, toEnt, relName, joins);
	}

	public static String addToOne(ObjEntity from, ObjEntity to, String relName,
			String... joins) {
		if (joins.length % 2 != 0)
			throw new RuntimeException("Wrong number of joins (" + joins.length
					+ ")");

		DbEntity fromDb = from.getDbEntity();
		DbEntity toDb = to.getDbEntity();

		String relNameDb = nameTranslator.translatePropertyName(relName);
		if (fromDb.getAttribute(relNameDb) != null)
			relNameDb = "_" + relNameDb;

		DbRelationship dbFromTo = new DbRelationship(relNameDb);
		dbFromTo.setSourceEntity(fromDb);
		dbFromTo.setTargetEntity(toDb);
		fromDb.addRelationship(dbFromTo);

		for (int i = 0; i < joins.length; i += 2) {
			String sourceCol = joins[i];
			String targetCol = joins[i + 1];
			requireColumn(fromDb, sourceCol);
			requireColumn(toDb, targetCol);

			DbJoin join = new DbJoin(dbFromTo);
			join.setSourceName(sourceCol);
			join.setTargetName(targetCol);
			join.setRelationship(dbFromTo);
			dbFromTo.addJoin(join);
		}

		String revName = "REV_" + fromDb.getName() + "_" + relNameDb;
		if (toDb.getRelationship(revName) == null) {
			DbRelationship dbToFrom = dbFromTo.createReverseRelationship();
			dbToFrom.setName(revName);
			toDb.addRelationship(dbToFrom);
		}

		ObjRelationship objRel = new ObjRelationship(relName);
		objRel.setDbRelationshipPath(dbFromTo.getName());
		objRel.setSourceEntity(from);
		objRel.setTargetEntity(to);
		from.addRelationship(objRel);

		return revName;
	}

	private static void requireColumn(DbEntity dbEntity, String columnName) {
		if (dbEntity.getAttribute(columnName) == null)
			throw new RuntimeException("No such column: " + dbEntity.getName()
					+ "." + columnName);
	}

	public static DbRelationship addReverse(ObjEntity sourceEntity,
			String sourceDbRel, String revDbRel) {
		DbRelationship sourceRel = (DbRelationship) sourceEntity.getDbEntity()
				.getRelationship(sourceDbRel);
		DbRelationship reverseRel = sourceRel.createReverseRelationship();
		reverseRel.setName(revDbRel);
		reverseRel.getSourceEntity().addRelationship(reverseRel);
		return reverseRel;
	}

}
