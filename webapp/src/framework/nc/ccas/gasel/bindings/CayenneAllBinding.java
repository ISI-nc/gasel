package nc.ccas.gasel.bindings;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.query.SelectQuery;
import org.apache.hivemind.Location;
import org.apache.tapestry.coerce.ValueConverter;

public class CayenneAllBinding extends AbstractCayenneAllBinding {

	private static final ThreadLocal<Map<String, Set<DataObject>>> CACHE = new ThreadLocal<Map<String, Set<DataObject>>>() {
		@Override
		protected Map<String, Set<DataObject>> initialValue() {
			return new HashMap<String, Set<DataObject>>();
		}
	};

	public static void clearCache() {
		CACHE.get().clear();
	}

	public static <T extends Persistent> Set<T> query(ObjectContext context,
			Class<T> clazz, String... prefetches) {
		// Récupération des résultats
		String entityName = context.getEntityResolver() //
				.lookupObjEntity(clazz).getName();

		Set<DataObject> values = query(context, entityName, prefetches);

		// Construction du Set<T>
		HashSet<T> result = new HashSet<T>(values.size());
		for (Persistent value : values) {
			result.add(clazz.cast(value));
		}
		// fin
		return result;
	}

	public static Set<DataObject> query(ObjectContext context, String entityName,
			String... prefetches) {
		Map<String, Set<DataObject>> cache = CACHE.get();
		Set<DataObject> cachedValues;
		if (cache.containsKey(entityName)) {
			// Utilisation de la valeur en cache
			cachedValues = cache.get(entityName);
			CommonQueries.prefetch(cachedValues, prefetches);
		} else {
			// Requète
			SelectQuery query = new SelectQuery(entityName);
			Collection<?> queryResults = context.performQuery(query);
			if (prefetches != null && prefetches.length > 0)
				CommonQueries.addPrefetches(query, entityName, prefetches);
			// création de la valeur pour le cache
			cachedValues = new HashSet<DataObject>(queryResults.size());
			for (Object queryResult : queryResults) {
				cachedValues.add((DataObject) queryResult);
			}
			// enregistrement dans le cache
			cache.put(entityName, cachedValues);
		}
		// Création du résultat (copie locale des valeurs)
		Set<DataObject> values = new HashSet<DataObject>(cachedValues.size());
		for (DataObject cachedValue : cachedValues) {
			DataObject localValue = (DataObject) context.localObject(cachedValue.getObjectId(), cachedValue);
			values.add(localValue);
		}
		return values;
	}

	protected CayenneAllBinding(String entity, String description,
			ValueConverter valueConverter, Location location) {
		super(entity, description, valueConverter, location);
	}

}
