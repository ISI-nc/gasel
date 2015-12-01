package nc.ccas.gasel.jwcs.core.search.critere;

import java.util.List;

import nc.ccas.gasel.BasePagePsm;
import nc.ccas.gasel.BasePageSort;
import nc.ccas.gasel.jwcs.core.search.Critere;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.PropertySelection;

import com.asystan.common.cayenne_new.QueryFactory;

public abstract class EgalObjet extends Critere {

	@Parameter(required = true)
	public abstract List<? extends DataObject> getValues();

	@Component(type = "PropertySelection", bindings = { "value=value",
			"model=model" })
	public abstract PropertySelection getSelect();

	@Persist("workflow")
	public abstract DataObject getValue();

	private final BasePagePsm psm = new BasePagePsm(this);

	public IPropertySelectionModel getModel() {
		return psm.withNull("Tous", psm
				.dataObject(new BasePageSort<DataObject>(getValues())
						.byToString().results()));
	}

	@Override
	protected void renderImpl(IMarkupWriter writer, IRequestCycle cycle) {
		getSelect().render(writer, cycle);
	}

	@Override
	public Expression buildExpressionImpl(String path) {
		if (getValue() == null) {
			return null;
		}
		return QueryFactory.createEquals(path, getValue());
	}

	@Override
	protected void prepareForRender(IRequestCycle cycle) {
		super.prepareForRender(cycle);
		getValues();
	}

}
