package nc.ccas.gasel.jwcs.core.edit;

import nc.ccas.gasel.psm.IntRangePSM;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.PropertySelection;
import org.apache.tapestry.form.TextField;

public abstract class Annee extends AbstractFormComponent {

	@Parameter(required = true)
	public abstract Number getValue();

	@Parameter(name = "min")
	public abstract Integer getAnneeMin();

	@Parameter(name = "max")
	public abstract Integer getAnneeMax();

	@Parameter(name = "onchange")
	public abstract String getOnChange();

	@Component(bindings = { "value=ognl:value",
			"translator=translator:number,omitZero" }, inheritInformalParameters = true)
	public abstract TextField getField();

	@Component(bindings = { "value=ognl:value", "model=ognl:modeleAnnee",
			"onchange=ognl:onChange" }, inheritInformalParameters = true)
	public abstract PropertySelection getSelect();

	@Override
	protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		if (getAnneeMin() != null && getAnneeMax() != null) {
			getSelect().render(writer, cycle);
		} else {
			getField().render(writer, cycle);
		}
	}

	@Override
	protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		renderFormComponent(writer, cycle);
	}

	public IPropertySelectionModel getModeleAnnee() {
		return new IntRangePSM(getAnneeMin(), getAnneeMax());
	}

}
