package nc.ccas.gasel.psm;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nc.ccas.gasel.bindings.EnumerationBinding;
import nc.ccas.gasel.model.Enumeration;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.access.DataContext;
import org.apache.tapestry.form.IPropertySelectionModel;

public class EnumPSM implements IPropertySelectionModel {

	private final ObjectContext context;

	private final Class<?> valueClass;

	private final List<?> values;

	private final String nullLabel;

	public EnumPSM(String nullLabel, DataContext context,
			Class<? extends Persistent> valueClass) {
		this(nullLabel, EnumerationBinding.query(context, valueClass), context,
				valueClass);
	}

	public EnumPSM(String nullLabel, List<?> values, DataContext context) {
		this(nullLabel, values, context, values.get(0).getClass());
	}

	public EnumPSM(String nullLabel, List<?> values, ObjectContext context,
			Class<?> valueClass) {
		this.nullLabel = nullLabel;
		this.values = values;
		this.context = context;
		this.valueClass = valueClass;
		// On trie les valeurs
		Collections.sort(values, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				Enumeration e1 = getEnum(o1);
				Enumeration e2 = getEnum(o2);
				return e1.getLibelle().compareToIgnoreCase(e2.getLibelle());
			}
		});
	}

	public Object getOption(int index) {
		if (nullLabel == null) {
			return values.get(index);
		}
		if (index == 0) {
			return null;
		}
		return values.get(index - 1);
	}

	public int getOptionCount() {
		if (nullLabel == null) {
			return values.size();
		}
		return values.size() + 1;
	}

	// Affichage

	public String getLabel(int index) {
		Object obj = getOption(index);
		if (obj == null) {
			return nullLabel;
		}
		Enumeration enumeration = getEnum(obj);
		return enumeration.getLibelle();
	}

	// Sérialisation HTML

	public String getValue(int index) {
		Object obj = getOption(index);
		if (obj == null) {
			return "X";
		}
		EnumEmul enumeration = getEnum(obj);
		return enumeration.getId().toString();
	}

	public Object translateValue(String value) {
		if (value == null || value.equals("X") || valueClass == null) {
			return null;
		}
		return DataObjectUtils.objectForPK(context, valueClass, Integer
				.parseInt(value));
	}

	public boolean isDisabled(int index) {
		return false;
	}

	// -----

	private EnumEmul getEnum(Object obj) {
		// if (!(obj instanceof Enumeration)) {
		// Simulation d'une énumération nécessaire
		return new EnumEmul((DataObject) obj);
		// }
		// return (Enumeration) obj;
	}
}

class EnumEmul implements Enumeration {
	private final DataObject obj;

	public EnumEmul(DataObject obj) {
		this.obj = obj;
	}

	public Integer getId() {
		return DataObjectUtils.intPKForObject(obj);
	}

	public String getLibelle() {
		return (String) obj.readProperty("libelle");
		// return (String) PropertyUtils.read(obj, "libelle");
	}

}