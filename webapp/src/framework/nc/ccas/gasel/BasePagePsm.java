package nc.ccas.gasel;

import static nc.ccas.gasel.bindings.CayenneAllBinding.query;

import java.util.ArrayList;
import java.util.Collection;

import nc.ccas.gasel.psm.DataObjectPSM;
import nc.ccas.gasel.psm.IntRangePSM;
import nc.ccas.gasel.psm.MoisPSM;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.DataObject;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.form.BeanPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.services.DataSqueezer;

public class BasePagePsm {

	public final IPropertySelectionModel mois = MoisPSM.INSTANCE;

	private final IComponent component;

	public BasePagePsm(IComponent component) {
		this.component = component;
	}

	public IPropertySelectionModel empty() {
		return new EmptyPSM();
	}

	public IPropertySelectionModel ouiNon = bool("Oui", "Non");

	public IPropertySelectionModel bool(String trueLabel, String falseLabel) {
		return new BooleanPSM(trueLabel, falseLabel);
	}

	public IPropertySelectionModel range(int from, int to) {
		return new IntRangePSM(from, to);
	}

	public IPropertySelectionModel string(Collection<String> values) {
		return new StringPropertySelectionModel((String[]) values
				.toArray(new String[values.size()]));
	}

	public IPropertySelectionModel bean(Collection<Object> values,
			String property) {
		return new BeanPropertySelectionModel(values, property);
	}

	/* dataObject */

	public IPropertySelectionModel dataObject(
			Collection<? extends DataObject> values) {
		return new DataObjectPSM(new ArrayList<DataObject>(values), squeezer());
	}

	public IPropertySelectionModel dataObject(
			Collection<? extends DataObject> values, String property) {
		return new DataObjectPSM(new ArrayList<DataObject>(values), property,
				squeezer());
	}

	protected DataSqueezer squeezer() {
		return component.getPage().getRequestCycle().getInfrastructure()
				.getDataSqueezer();
	}

	/* withNull */

	public IPropertySelectionModel withNull(String nullLabel,
			IPropertySelectionModel psm) {
		return new WithNullPSM(nullLabel, psm);
	}

	public IPropertySelectionModel withNull(IPropertySelectionModel psm) {
		return withNull("--", psm);
	}

	/* specific */

	public IPropertySelectionModel all(String clazz) {
		return all(clazz, null);
	}

	public IPropertySelectionModel all(String clazz, String sortProperty) {
		return all(clazz, sortProperty, null);
	}

	public IPropertySelectionModel all(String entityName, String sortProperty,
			String nullLabel) {
		Collection<? extends DataObject> values = query( //
				DataContext.getThreadDataContext(), entityName);
		return sortAndBuild(values, sortProperty, null);
	}

	private IPropertySelectionModel sortAndBuild(
			Collection<? extends DataObject> values, String sortProperty,
			String nullLabel) {
		BasePageSort<DataObject> sort = new BasePageSort<DataObject>(values);

		if (sortProperty != null) {
			sort.by(sortProperty);
		}
		sort.byToString();

		return withNull(nullLabel, dataObject(sort.results()));
	}

}

class WithNullPSM implements IPropertySelectionModel {

	private final IPropertySelectionModel psm;

	private final String nullLabel;

	public WithNullPSM(String nullLabel, IPropertySelectionModel psm) {
		this.nullLabel = nullLabel;
		this.psm = psm;
	}

	public int getOptionCount() {
		return psm.getOptionCount() + 1;
	}

	public String getLabel(int index) {
		if (index == 0) {
			return nullLabel;
		}
		return psm.getLabel(index - 1);
	}

	public String getValue(int index) {
		if (index == 0) {
			return "X";
		}
		return psm.getValue(index - 1);
	}

	public Object getOption(int index) {
		if (index == 0) {
			return null;
		}
		return psm.getOption(index - 1);
	}

	public Object translateValue(String value) {
		if ("X".equals(value)) {
			return null;
		}
		return psm.translateValue(value);
	}

	public boolean isDisabled(int index) {
		return false;
	}

}

class BooleanPSM implements IPropertySelectionModel {

	private final String trueLabel;

	private final String falseLabel;

	public BooleanPSM(String trueLabel, String falseLabel) {
		this.trueLabel = trueLabel;
		this.falseLabel = falseLabel;
	}

	public String getLabel(int index) {
		return index == 0 ? trueLabel : falseLabel;
	}

	public Object getOption(int index) {
		return index == 0;
	}

	public int getOptionCount() {
		return 2;
	}

	public String getValue(int index) {
		return index == 0 ? "T" : "F";
	}

	public Object translateValue(String value) {
		return "T".equals(value);
	}

	public boolean isDisabled(int index) {
		return false;
	}

}

class EmptyPSM implements IPropertySelectionModel {
	public String getLabel(int index) {
		return null;
	}

	public Object getOption(int index) {
		return null;
	}

	public int getOptionCount() {
		return 0;
	}

	public String getValue(int index) {
		return null;
	}

	public Object translateValue(String value) {
		return null;
	}

	public boolean isDisabled(int index) {
		return false;
	}
}