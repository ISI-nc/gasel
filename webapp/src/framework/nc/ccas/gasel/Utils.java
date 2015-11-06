package nc.ccas.gasel;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.cayenne.DataChannel;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;

public class Utils {

	public static boolean isTestMode() {
		return "true".equals(System
				.getProperty("org.apache.tapestry.disable-caching"));
	}

	/*
	 * Dates
	 */

	public static Date today() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		return gc.getTime();
	}

	/*
	 * DataObject/DataContext
	 */

	public static <T extends DataObject> T ensureDataContext(T o,
			ObjectContext context) {
		if (o == null)
			return null;
		if (context == o.getObjectContext()) {
			return o;
		}
		T obj = (T) context.localObject(o.getObjectId(), o);
		obj.setPersistenceState(o.getPersistenceState());
		return obj;
		// if (isParent(context, o.getObjectContext())) {
		// // objet dans un sous-contexte => il remonte vers un parent
		// T obj = (T) context.localObject(o.getObjectId(), o);
		// // importRels(obj, o);
		// return obj;
		// }
		// if (isParent(o.getObjectContext(), context)) {
		// // objet dans un contexte parent => il descend
		// return (T) context.localObject(o.getObjectId(), o);
		// }
		// if (o.getObjectId().isTemporary()) {
		// throw new RuntimeException("Impossible d'importer un objet à "
		// + "l'état NEW en dehors de contextes parents: " + o);
		// }
		// // On doit importer l'objet dans le nouveau contexte
		// switch (o.getPersistenceState()) {
		// case PersistenceState.TRANSIENT:
		// context.registerNewObject(o);
		// return o;
		// case PersistenceState.COMMITTED:
		// case PersistenceState.HOLLOW:
		// case PersistenceState.MODIFIED:
		// return (T) DataObjectUtils.objectForPK(context, o.getObjectId());
		// case PersistenceState.NEW:
		// throw new RuntimeException("Unreachable");
		// case PersistenceState.DELETED:
		// throw new RuntimeException(
		// "PersistenceState non supporté : DELETED");
		// default:
		// throw new RuntimeException("PersistenceState inconnu : "
		// + o.getPersistenceState());
		// }
	}

	public static boolean isParent(DataContext parent, DataContext child) {
		DataContext dc = child;
		for (;;) {
			if (dc == parent) {
				return true;
			}
			DataChannel channel = dc.getChannel();
			if (channel instanceof DataContext) {
				dc = (DataContext) channel;
			} else {
				return false;
			}
		}
	}

}
