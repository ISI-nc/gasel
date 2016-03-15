package nc.ccas.gasel.modelUtils;

import static com.asystan.common.cayenne_new.QueryFactory.createAnd;
import static com.asystan.common.cayenne_new.QueryFactory.createIn;
import static com.asystan.common.cayenne_new.QueryFactory.createNot;
import static com.asystan.common.cayenne_new.QueryFactory.createOr;
import static nc.ccas.gasel.cayenne.MairieExpr.createTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import nc.ccas.gasel.cayenne.MairieExpr;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.DbEntity;
import org.apache.cayenne.map.DbRelationship;
import org.apache.cayenne.map.ObjAttribute;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.cayenne.query.SelectQuery;

import com.asystan.common.StringUtils;

/**
 * 
 * Synchronise les valeurs "locked" d'une enumeration.
 * 
 * @author Mikaël Cluseau - ISI.NC
 * 
 */
public class EnumerationSync {

	public static void syncAll() {
		for (EnumerationSync sync : INSTANCES.values()) {
			sync.sync();
		}
	}

	public static Set<Class<?>> syncableClasses() {
		return INSTANCES.keySet();
	}

	public static void sync(Class<?> clazz) {
		INSTANCES.get(clazz).sync();
	}

	public static boolean synced(Class<?> clazz) {
		return INSTANCES.get(clazz).syncDone;
	}

	private static final Map<Class<?>, EnumerationSync> INSTANCES = new HashMap<Class<?>, EnumerationSync>();

	private Map<Integer, EnumInfo> values = new TreeMap<Integer, EnumInfo>();

	private final Class<? extends DataObject> clazz;

	private boolean syncDone = false;

	public EnumerationSync(Class<? extends DataObject> clazz) {
		INSTANCES.put(clazz, this);
		this.clazz = clazz;
	}

	public void add(int id, String libelle) {
		this.add(id, libelle, null);
	}

	public void add(int id, String libelle, Integer parentId,
			Object... extraValues) {
		if (values.containsKey(id)) {
			throw new RuntimeException("Doublon sur l'id " + id);
		}
		EnumInfo info = new EnumInfo();
		info.libelle = libelle;
		info.parentId = parentId;

		for (int i = 0; i < extraValues.length; i += 2) {
			info.extraValues.put((String) extraValues[i], extraValues[i + 1]);
		}

		values.put(id, info);
	}

	@SuppressWarnings("unchecked")
	private void sync() {
		if (syncDone)
			return;

		DataContext dc = CayenneUtils.createDataContext();

		// Préation de la gestion du parent
		ObjEntity obje = dc.getEntityResolver().lookupObjEntity(clazz);
		DbEntity dbe = obje.getDbEntity();
		boolean has_parent = (obje.getRelationship("parent") != null);
		Class<?> parentClass = null;
		if (has_parent) {
			// On attend la synchro du parent
			ObjEntity target = (ObjEntity) obje.getRelationship("parent")
					.getTargetEntity();
			parentClass = target.getJavaClass();
			// Classe parente chargée, donc syncher instancié
			if (INSTANCES.containsKey(parentClass)) {
				sync(parentClass);
			}
		}

		Set<Integer> ids = new TreeSet<Integer>(values.keySet());

		// Suppression des éléments absents
		List<?> objectsToRemove = CommonQueries.select(
				clazz,
				createAnd(createTrue("locked"),
						createNot(createIn("db:id", ids))));
		dc.deleteObjects(objectsToRemove);

		// Synchronisation des éléments existants
		List<DataObject> results = dc.performQuery(new SelectQuery(clazz,
				createOr(createTrue("locked"), createIn("db:id", ids))));
		for (DataObject result : results) {
			int id = DataObjectUtils.intPKForObject(result);
			EnumInfo info = values.get(id);
			for (Entry<String, Object> extraValue : info.parametersMap()
					.entrySet()) {
				setValue(result, extraValue.getKey(), extraValue.getValue());
			}
			result.writeProperty("locked", true);
			ids.remove(id);
		}
		dc.commitChanges();

		// Création des éléments manquants
		for (Integer id : ids) {
			EnumInfo info = values.get(id);
			SQLTemplate insertTemplate = new SQLTemplate(dbe,
					info.insertTemplate(dbe));
			Map<String, Object> parameters = info.parametersMap(id, true);
			insertTemplate.setParameters(parameters);
			dc.performNonSelectingQuery(insertTemplate);
		}
		syncDone = true;
	}

	private void setValue(DataObject object, String attrName, Object value) {
		ObjectContext dc = object.getObjectContext();
		ObjEntity obje = dc.getEntityResolver().lookupObjEntity(object);
		DbEntity dbe = obje.getDbEntity();

		DbAttribute dba = (DbAttribute) dbe.getAttribute(attrName);
		ObjAttribute attr = obje.getAttributeForDbAttribute(dba);
		if (attr != null) {
			object.writeProperty(attr.getName(), value);
		} else {
			DbRelationship dbRel = relationshipForAttribute(dbe, attrName);
			if (dbRel == null) {
				throw new IllegalArgumentException(
						"Pas d'attribut ni de relation (toOne) lié à "
								+ attrName);
			}
			ObjRelationship objRel = obje
					.getRelationshipForDbRelationship(dbRel);
			DataObject target;
			if (value == null) {
				target = null;
			} else {
				target = (DataObject) DataObjectUtils.objectForPK(dc,
						objRel.getTargetEntityName(), value);
			}
			object.setToOneTarget(objRel.getName(), target, false);
		}
	}

	private DbRelationship relationshipForAttribute(DbEntity dbe,
			String attrName) {
		Collection<DbRelationship> rels = dbe.getRelationships();
		for (DbRelationship rel : rels) {
			if (rel.isToMany())
				continue; // forcément toOne
			Collection<DbAttribute> dbas = rel.getSourceAttributes();
			for (DbAttribute dba : dbas) {
				if (dba.getName().equals(attrName)) {
					return rel;
				}
			}
		}
		return null;
	}

}

class EnumInfo {
	String libelle;

	Integer parentId;

	Map<String, Object> extraValues = new HashMap<String, Object>();

	Map<String, Object> parametersMap() {
		return parametersMap(false);
	}

	Map<String, Object> parametersMap(boolean escape) {
		Map<String, Object> retval = new HashMap<String, Object>();

		retval.put("actif", MairieExpr.trueValue());
		retval.put("locked", MairieExpr.trueValue());
		for (Entry<String, Object> entry : extraValues.entrySet()) {
			Object value = entry.getValue();
			if (escape && value instanceof String) {
				value = SqlUtils.escape((String) value);
			}
			retval.put(entry.getKey(), value);
		}
		retval.put("libelle", escape ? SqlUtils.escape(libelle) : libelle);
		if (parentId != null) {
			retval.put("parent_id", parentId);
		}

		return retval;
	}

	Map<String, Object> parametersMap(int id) {
		return parametersMap(id, false);
	}

	Map<String, Object> parametersMap(int id, boolean escape) {
		Map<String, Object> retval = parametersMap(escape);
		retval.put("id", id);
		return retval;
	}

	String insertTemplate(DbEntity dbe) {
		Map<String, Object> map = parametersMap();
		StringBuffer buf = new StringBuffer();

		buf.append("INSERT INTO ").append(dbe.getFullyQualifiedName());
		buf.append(" (id,").append(StringUtils.join(",", map.keySet()));
		buf.append(") VALUES ($id,$").append(
				StringUtils.join(",$", map.keySet()));
		buf.append(")");

		return buf.toString();
	}

}