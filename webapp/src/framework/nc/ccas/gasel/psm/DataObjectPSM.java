package nc.ccas.gasel.psm;

import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.query.SelectQuery;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.services.DataSqueezer;

public class DataObjectPSM implements IPropertySelectionModel {

	private final List<? extends DataObject> values;

	private final String nullLabel;

	private final String labelProperty;

	private final DataSqueezer squeezer;

	public DataObjectPSM(Class<? extends DataObject> clazz,
			DataSqueezer squeezer) {
		this(new SelectQuery(clazz), squeezer);
	}

	public DataObjectPSM(String objEntity, DataSqueezer squeezer) {
		this(new SelectQuery(objEntity), squeezer);
	}

	@SuppressWarnings("unchecked")
	public DataObjectPSM(SelectQuery query, DataSqueezer squeezer) {
		this(DataContext.getThreadDataContext().performQuery(query), squeezer);
	}

	public DataObjectPSM(List<? extends DataObject> values,
			DataSqueezer squeezer) {
		this(values, null, squeezer);
	}

	public DataObjectPSM(List<? extends DataObject> values,
			String labelProperty, DataSqueezer squeezer) {
		this(values, labelProperty, null, squeezer);
	}

	public DataObjectPSM(List<? extends DataObject> values,
			String labelProperty, String nullLabel, DataSqueezer squeezer) {
		this.values = values;
		this.nullLabel = nullLabel;
		this.labelProperty = labelProperty;
		this.squeezer = squeezer;
	}

	public String getLabel(int index) {
		Object object = getOption(index);
		if (object == null) {
			return nullLabel;
		}
		if (labelProperty == null) {
			return object.toString();
		}
		return PropertyUtils.read(object, labelProperty).toString();
	}

	public Object getOption(int index) {
		if (nullLabel != null && index == 0) {
			return null;
		}
		return values.get(index - (nullLabel == null ? 0 : 1));
	}

	public int getOptionCount() {
		return (nullLabel == null ? 0 : 1) + values.size();
	}

	public String getValue(int index) {
		return squeezer.squeeze(getOption(index));
	}

	public Object translateValue(String value) {
		if (value == null)
			return null;
		return squeezer.unsqueeze(value);
	}

	public boolean isDisabled(int index) {
		return false;
	}

}
