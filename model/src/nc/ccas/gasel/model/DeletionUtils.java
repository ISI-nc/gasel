package nc.ccas.gasel.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.Persistent;

public class DeletionUtils {

	// private static final ThreadLocal<Stack<String>> ST = new
	// ThreadLocal<Stack<String>>() {
	// protected Stack<String> initialValue() {
	// return new Stack<String>();
	// }
	// };

	public static void delete(Collection<? extends Persistent> objects) {
		for (Persistent value : objects) {
			if (value == null)
				return;
			// ST.get().push(
			// value.getClass().getSimpleName() + "#"
			// + intPKForObject(value)); // + ": " + value);
			// System.err.println("[DELE] " + StringUtils.join(" -> ",
			// ST.get()));

			if (value.getPersistenceState() == PersistenceState.DELETED)
				continue; // déjà supprimé, faut pas masquer les bugs...

			if (value instanceof ComplexDeletion) {
				((ComplexDeletion) value).prepareForDeletion();
			}
			value.getObjectContext().deleteObject(value);

			// ST.get().pop();
		}
	}

	public static void delete(DataObject... values) {
		delete(Arrays.asList(values));
	}

	public static void delete(DataObject object, String propertyName) {
		DataObject value = (DataObject) object.readProperty(propertyName);
		delete(value);
	}

	public static void empty(DataObject object, String... propertyNames) {
		for (String property : propertyNames) {
			List<?> list = (List<?>) object.readProperty(property);
			list = new ArrayList<Object>(list);
			for (Object o : list) {
				object.removeToManyTarget(property, (DataObject) o, true);
			}
		}
	}

}
