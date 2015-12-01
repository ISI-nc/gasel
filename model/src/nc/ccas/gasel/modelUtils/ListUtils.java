package nc.ccas.gasel.modelUtils;

import java.util.List;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.Fault;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.map.ObjRelationship;
import org.apache.cayenne.query.SelectQuery;

import com.asystan.common.cayenne_new.QueryFactory;

public class ListUtils {

	public static DataObject min(DataObject source, String liste,
			String property) {
		return minmax(source, liste, property, true);
	}

	public static DataObject max(DataObject source, String liste,
			String property) {
		return minmax(source, liste, property, false);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static DataObject minmax(DataObject source, String liste,
			String property, boolean min) {

		if (source.readPropertyDirectly(liste) instanceof Fault) {
			// Requète
			ObjRelationship rel = CayenneUtils.relation(source, liste);
			Expression expr = QueryFactory.createEquals(rel
					.getReverseRelationshipName(), source);
			SelectQuery q = new SelectQuery(rel.getTargetEntityName(), expr);
			q.addOrdering(property, min);

			return CommonQueries.unique(source.getObjectContext(), q);
		} else {
			// Recherche dans la liste
			return minmax((List) source.readPropertyDirectly(liste), property,
					min);
		}
	}

	public static DataObject min(List<? extends DataObject> liste,
			String property) {
		return minmax(liste, property, true);
	}

	public static DataObject max(List<? extends DataObject> liste,
			String property) {
		return minmax(liste, property, false);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T extends Comparable> DataObject minmax(
			List<? extends DataObject> liste, String property, boolean min) {
		DataObject retval = null;
		T ref = null;

		for (DataObject o : liste) {
			T other = (T) o.readProperty(property);
			if (other == null)
				continue;
			if ((ref == null) // Pas encore de ref
					// min & other inférieur
					|| (min && other.compareTo(ref) < 0)
					// max & other supérieur
					|| (!min && other.compareTo(ref) > 0)) {
				// mise à jour de la valeur
				ref = other;
				retval = o;
			}
		}
		return retval;
	}

}
