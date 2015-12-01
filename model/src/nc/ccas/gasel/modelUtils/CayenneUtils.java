package nc.ccas.gasel.modelUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.DbEntity;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;

public class CayenneUtils {
	
	public static DataDomain getDomain() {
		return Configuration.getSharedConfiguration().getDomain();
	}

	public static EntityResolver entityResolver() {
		return getDomain().getEntityResolver();
	}

	public static String pkCol(Persistent object) {
		return pkCol(object.getClass());
	}

	public static String pkCol(Class<? extends Persistent> clazz) {
		DbEntity entity = entityResolver().lookupObjEntity(clazz).getDbEntity();
		@SuppressWarnings("unchecked")
		Collection<DbAttribute> pkAttrs = entity.getPrimaryKey();
		if (pkAttrs.size() == 0) {
			return null;
		} else if (pkAttrs.size() > 1) {
			throw new IllegalArgumentException(
					"Multiple columns in PK (entity: " + entity + ")");
		}
		return ((DbAttribute) pkAttrs.iterator().next()).getName();
	}

	public static DataContext createDataContext() {
		return DataContext.createDataContext();
	}

	// ------

	public static ObjRelationship relation(DataObject object,
			String relationName) {
		ObjRelationship rel = (ObjRelationship) resolveEntity(object)
				.getRelationship(relationName);
		if (rel == null) {
			throw new RuntimeException("No relation \"" + relationName
					+ "\" in " + object.getClass());
		}
		return rel;
	}

	// ------

	public static ObjEntity resolveEntity(DataObject object) {
		return entityResolver().lookupObjEntity(object);
	}

	public static ObjEntity resolveEntity(String entityName) {
		return entityResolver().getObjEntity(entityName);
	}

	// ------

	public static String tableFor(Class<? extends DataObject> clazz) {
		DbEntity ent = entityResolver().lookupObjEntity(clazz).getDbEntity();
		if (ent == null) {
			throw new IllegalArgumentException("Pas de table pour " + clazz);
		}
		return ent.getFullyQualifiedName();
	}

	public static List<Integer> collectIds(List<? extends Persistent> list) {
		List<Integer> ids = new ArrayList<Integer>(list.size());
		for (Persistent p : list) {
			if (p.getObjectId().isTemporary())
				throw new IllegalArgumentException("ObjectId temporaire pour "
						+ p);
			ids.add(DataObjectUtils.intPKForObject(p));
		}
		return ids;
	}

}
