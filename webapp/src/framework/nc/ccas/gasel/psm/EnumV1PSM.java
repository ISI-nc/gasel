package nc.ccas.gasel.psm;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nc.ccas.gasel.model.v1.EnumerationV1;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.form.IPropertySelectionModel;

public class EnumV1PSM implements IPropertySelectionModel {

	private ObjectContext context;

	private final Class<? extends DataObject> valueClass;

	private final List<?> values;

	public EnumV1PSM(List<?> values, ObjectContext context,
			Class<? extends DataObject> valueClass) {
		this.values = values;
		this.context = context;
		this.valueClass = valueClass;
		// On trie les valeurs
		Collections.sort(values, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				EnumerationV1 e1 = getEnum(o1);
				EnumerationV1 e2 = getEnum(o2);
				return e1.getLabel().compareToIgnoreCase(e2.getLabel());
			}
		});
	}

	public Object getOption(int index) {
		if (index == 0) {
			return null;
		}
		return values.get(index - 1);
	}

	public int getOptionCount() {
		return values.size() + 1;
	}

	// Affichage

	public String getLabel(int index) {
		Object obj = getOption(index);
		if (obj == null) {
			return "--";
		}
		EnumerationV1 enumeration = getEnum(obj);
		return enumeration.getLabel();
	}

	// Sérialisation HTML

	public String getValue(int index) {
		Object obj = getOption(index);
		if (obj == null) {
			return "";
		}
		EnumerationV1 enumeration = getEnum(obj);
		return enumeration.getKey();
	}

	public Object translateValue(String value) {
		if (value.equals("")) {
			return null;
		}
		return CommonQueries.unique(context, valueClass, "enumeration.key",
				value);
	}

	public boolean isDisabled(int index) {
		return false;
	}

	// -----

	private EnumerationV1 getEnum(Object obj) {
		return (EnumerationV1) PropertyUtils.read(obj, "enumeration");
	}

}
