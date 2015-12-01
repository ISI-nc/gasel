package nc.ccas.gasel.modelUtils;

import static com.asystan.common.cayenne.QueryFactory.createIn;
import static nc.ccas.gasel.modelUtils.CayenneUtils.collectIds;
import static org.apache.cayenne.query.PrefetchTreeNode.JOINT_PREFETCH_SEMANTICS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.DataRow;
import org.apache.cayenne.Fault;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.ObjectId;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.DbJoin;
import org.apache.cayenne.map.DbRelationship;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;
import org.apache.cayenne.query.PrefetchTreeNode;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.cayenne.query.SelectQuery;
import org.apache.commons.lang.NotImplementedException;

import com.asystan.common.cayenne.QueryFactory;

public class CommonQueries {

	public static DataContext createAndBindThreadContext() {
		DataContext context = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(context);
		return context;
	}

	/*
	 * Recup par id
	 */

	public static <T extends Persistent> T findById(Class<T> clazz, int id) {
		return findById(clazz, id, dc());
	}

	public static <T extends Persistent> T findById(Class<T> clazz, int id,
			ObjectContext dc) {
		return clazz.cast(DataObjectUtils.objectForPK(dc, clazz, id));
	}

	/*
	 * Récupérations par champs uniques
	 */

	public static <T> T unique(ObjectContext dc, Class<T> clazz,
			String property, Object value) {
		return unique(dc, clazz, QueryFactory.createEquals(property, value));
	}

	public static <T> T unique(ObjectContext dc, Class<T> clazz, Expression expr) {
		return clazz.cast(unique(dc,
				dc.getEntityResolver().lookupObjEntity(clazz).getName(), expr));
	}

	public static DataObject unique(ObjectContext dc, String entityName,
			String property, Object value) {
		return unique(dc, entityName,
				QueryFactory.createEquals(property, value));
	}

	public static DataObject unique(ObjectContext dc, String entityName,
			Expression expr) {
		SelectQuery query = new SelectQuery(entityName, expr);
		return unique(dc, query);
	}

	@SuppressWarnings("unchecked")
	public static DataObject unique(ObjectContext dc, SelectQuery query) {
		query.setFetchLimit(1);
		List<? extends DataObject> results = dc.performQuery(query);
		if (results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}

	/*
	 * Récup de tout
	 */

	@SuppressWarnings("unchecked")
	public static <T extends Persistent> List<T> getAll(ObjectContext context,
			Class<T> clazz) {
		return context.performQuery(new SelectQuery(clazz));
	}

	public static <T extends Persistent> List<T> getAll(Class<T> clazz) {
		return getAll(dc(), clazz);
	}

	/*
	 * Select
	 */

	public static <T extends Persistent> List<T> select(Class<T> clazz,
			String expr, String... prefetches) {
		return select(dc(), clazz, expr, prefetches);
	}

	public static <T extends Persistent> List<T> select(Class<T> clazz,
			Expression expr, String... prefetches) {
		return select(dc(), clazz, expr, prefetches);
	}

	public static <T extends Persistent> List<T> select(ObjectContext context,
			Class<T> clazz, String expr, String... prefetches) {
		return select(context, clazz, Expression.fromString(expr), prefetches);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Persistent> List<T> select(ObjectContext context,
			Class<T> clazz, Expression expr, String... prefetches) {
		SelectQuery query = new SelectQuery(clazz, expr);

		for (String prefetch : prefetches)
			query.addPrefetch(prefetch);

		return context.performQuery(query);
	}

	/*
	 * Fakes
	 */

	public static <T> T fakeToOneGet(DataObject source, String sourceProperty,
			Class<T> destClass, String destProperty) {
		Object refToDest = source.readProperty(sourceProperty);
		if (refToDest == null) {
			return null;
		}
		return unique(source.getObjectContext(), destClass, destProperty,
				refToDest);
	}

	public static <T> void fakeToOneSet(DataObject source,
			String sourceProperty, DataObject dest, String destProperty) {
		source.writeProperty(sourceProperty,
				dest == null ? null : dest.readProperty(destProperty));
	}

	/*
	 * --- utils ---
	 */

	private static ObjectContext dc() {
		try {
			return DataContext.getThreadDataContext();
		} catch (IllegalStateException ise) {
			DataContext dc = CayenneUtils.createDataContext();
			DataContext.bindThreadDataContext(dc);
			return dc;
		}
	}

	private static EntityResolver er() {
		return dc().getEntityResolver();
	}

	public static Integer max(Class<? extends DataObject> clazz, String dbField) {
		return max(clazz, dbField, null);
	}

	public static Integer max(Class<? extends DataObject> clazz,
			String dbField, String whereClause) {
		return aggregationIntResult(clazz, dbField, "MAX", whereClause);
	}

	public static String maxString(Class<? extends DataObject> clazz,
			String dbField, String whereClause) {
		return (String) aggregationResult(clazz, dbField, "MAX", whereClause);
	}

	public static Integer min(Class<? extends DataObject> clazz, String dbField) {
		return min(clazz, dbField, null);
	}

	public static Integer min(Class<? extends DataObject> clazz,
			String dbField, String whereClause) {
		return aggregationIntResult(clazz, dbField, "MIN", whereClause);
	}

	private static Object aggregationResult(Class<? extends DataObject> clazz,
			String dbField, String function, String whereClause) {
		ObjectContext dc = dc();

		String tableName = dc.getEntityResolver().lookupObjEntity(clazz)
				.getDbEntity().getFullyQualifiedName();

		// Query
		String query = "SELECT " + function + "(" + dbField + ") AS v FROM "
				+ tableName;
		if (whereClause != null)
			query += " WHERE " + whereClause;

		// Template
		SQLTemplate template = new SQLTemplate(clazz, query);
		template.setFetchingDataRows(true);

		// Perform query
		DataRow row = (DataRow) dc.performQuery(template).get(0);
		return row.get("v");
	}

	private static Integer aggregationIntResult(
			Class<? extends DataObject> clazz, String dbField, String function,
			String whereClause) {
		Object value = aggregationResult(clazz, dbField, function, whereClause);

		// Return result
		if (value == null)
			return null;
		return ((Number) value).intValue();
	}

	// -----

	private static final int BLOCK_SIZE = 200;

	public static <T extends DataObject> void prefetch(Collection<T> liste,
			String... prefetches) {
		prefetch(dc(), liste, prefetches);
	}

	public static <T extends DataObject> void prefetch(ObjectContext context,
			Collection<T> liste, String... prefetches) {
		// Expansion des prefetches
		Set<String> expandedPrefetches = new TreeSet<String>();
		for (String prefetch : prefetches) {
			expandedPrefetches.addAll(expandPrefetch(prefetch));
		}

		// Filtrage des éléments qui n'ont pas besoin du prefetch
		// et des prefetches inutiles
		Set<String> usefulPrefetches = new TreeSet<String>();
		ArrayList<T> list = new ArrayList<T>(liste.size());
		for (T element : liste) {
			if (element.getObjectId().isTemporary())
				continue;

			boolean needPrefetch = false;
			for (String prefetch : prefetches) {
				if (needPrefetch(element, prefetch)) {
					usefulPrefetches.add(prefetch);
					needPrefetch = true;
				}
			}
			if (!needPrefetch)
				continue;
			list.add(element);
		}

		if (list.isEmpty())
			return;

		Class<?> clazz = list.get(0).getClass();
		Collection<DbAttribute> pk = pk(clazz);
		if (pk.size() != 1)
			throw new NotImplementedException("Pk size != 1 (== " + pk.size()
					+ ")");
		String pkField = pk.iterator().next().getName();

		ObjEntity entity = context.getEntityResolver().lookupObjEntity(clazz);

		for (int i = 0; i < list.size(); i += BLOCK_SIZE) {
			Expression expr = createIn("db:" + pkField, collectIds( //
					list.subList(i, Math.min(list.size(), i + BLOCK_SIZE))));
			SelectQuery query = new SelectQuery(clazz, expr);
			addExpandedPrefetches(expandedPrefetches, entity, query);
			context.performQuery(query);
		}
	}

	private static boolean needPrefetch(DataObject p, String path) {
		if (p.getPersistenceState() == PersistenceState.HOLLOW)
			return true;

		if (path.length() == 0)
			return false;

		String[] pathElements = path.split("\\.", 2);
		String root = pathElements[0];
		String subPath = pathElements.length > 1 ? pathElements[1] : "";

		Object property = p.readPropertyDirectly(root);
		if (property instanceof Fault) {
			return true; // needs prefetch, indeed ;)
		}
		// recurse otherwise
		if (property instanceof DataObject) {
			return needPrefetch((DataObject) property, subPath);
		}
		if (property instanceof Collection) {
			Collection<?> collection = (Collection<?>) property;
			for (Object element : collection) {
				if (needPrefetch((DataObject) element, subPath)) {
					return true;
				}
			}
			return false;
		}
		// It's something else we don't need to fetch (ie: String, Integer...)
		return false;
	}

	public static Collection<String> expandPrefetch(String prefetch) {
		List<String> expand = new ArrayList<String>();
		StringBuilder buf = new StringBuilder();
		for (String pathComponent : prefetch.split("\\.")) {
			if (buf.length() > 0)
				buf.append('.');
			buf.append(pathComponent);
			expand.add(buf.toString());
		}
		return expand;
	}

	public static boolean canUseJoinPrefetch(ObjEntity rootEntity, String path) {
		ObjEntity current = rootEntity;
		for (String pathElement : path.split("\\.")) {
			ObjRelationship relationship = (ObjRelationship) current
					.getRelationship(pathElement);

			if (relationship == null)
				throw new RuntimeException("No such relationship: " + current);

			if (relationship.isToMany())
				return false;
			if (!isMandatory(relationship))
				return false;

			current = (ObjEntity) relationship.getTargetEntity();
		}
		return true;
	}

	private static boolean isMandatory(ObjRelationship rel) {
		if (rel.getDbRelationships().isEmpty())
			return false;

		for (Object o : rel.getDbRelationships()) {
			DbRelationship dbRel = (DbRelationship) o;

			if (dbRel.getJoins().isEmpty())
				return false;

			for (Object o1 : dbRel.getJoins()) {
				DbJoin j = (DbJoin) o1;

				if (!j.getSource().isMandatory())
					return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private static Collection<DbAttribute> pk(Class<?> clazz) {
		return er().lookupObjEntity(clazz).getDbEntity().getPrimaryKey();
	}

	public static void addPrefetches(SelectQuery query,
			Class<? extends Persistent> entityClass, String... prefetches) {
		addPrefetches(query, er().lookupObjEntity(entityClass), prefetches);
	}

	public static void addPrefetches(SelectQuery query, String entityName,
			String... prefetches) {
		addPrefetches(query, er().getObjEntity(entityName), prefetches);
	}

	public static void addPrefetches(SelectQuery query, ObjEntity entity,
			String... prefetches) {
		Set<String> expandedPrefetches = new TreeSet<String>();
		for (String prefetch : prefetches) {
			expandedPrefetches.addAll(expandPrefetch(prefetch));
		}
		addExpandedPrefetches(expandedPrefetches, entity, query);
	}

	private static void addExpandedPrefetches(Set<String> expandedPrefetches,
			ObjEntity entity, SelectQuery query) {
		for (String prefetch : expandedPrefetches) {
			String path;
			boolean dontJoin;
			if (prefetch.endsWith("+")) {
				path = prefetch.substring(0, prefetch.length() - 1);
				dontJoin = true;
			} else {
				path = prefetch;
				dontJoin = false;
			}

			PrefetchTreeNode prefetchTreeNode = query.addPrefetch(path);
			if (!dontJoin && canUseJoinPrefetch(entity, path))
				prefetchTreeNode.setSemantics(JOINT_PREFETCH_SEMANTICS);
		}
	}

	public static <T extends Persistent> List<T> resolveIds(Class<T> clazz,
			int... ids) {
		List<T> results = new ArrayList<T>();
		for (int id : ids) {
			results.add(clazz.cast(DataObjectUtils.objectForPK(dc(), clazz, id)));
		}
		return results;
	}

	public static <T extends Persistent> T hollow(Class<T> clazz, int id) {
		T object;
		try {
			object = clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		object.setObjectId(new ObjectId(clazz.getSimpleName(), "id", id));
		object.setPersistenceState(PersistenceState.HOLLOW);
		return object;
	}

	public static <T extends Persistent> Map<Integer, T> idMap(
			Collection<? extends T> objects) {
		Map<Integer, T> map = new HashMap<Integer, T>();
		for (T object : objects) {
			map.put(DataObjectUtils.intPKForObject(object), object);
		}
		return map;
	}

}
