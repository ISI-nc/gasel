package nc.ccas.gasel.bindings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.property.PropertyUtils;
import org.apache.cayenne.query.Ordering;
import org.apache.hivemind.Location;
import org.apache.tapestry.coerce.ValueConverter;

public class EnumerationBinding extends AbstractCayenneAllBinding {

	private static final Comparator<Object> COMPARATOR = new Ordering(
			"libelle", true);

	public static <T extends Persistent> List<T> query(ObjectContext context,
			Class<T> clazz) {
		return filter(CayenneAllBinding.query(context, clazz));
	}

	public static List<DataObject> query(ObjectContext context, String entityName) {
		return filter(CayenneAllBinding.query(context, entityName));
	}

	private static <T extends Persistent> List<T> filter(Set<T> values) {
		List<T> retval = new ArrayList<T>(values.size());

		// Ajout sélectif
		for (T value : values) {
			Boolean actif = (Boolean) PropertyUtils.getProperty(value, "actif");
			if (actif == null || !actif)
				continue;
			retval.add(value);
		}

		// Tri
		Collections.sort(retval, COMPARATOR);
		return retval;
	}

	public EnumerationBinding(String entity, String description,
			ValueConverter valueConverter, Location location) {
		super(entity, description, valueConverter, location);
	}

	@Override
	public Object getObject() {
		return filter(super.getValues());
	}

}
