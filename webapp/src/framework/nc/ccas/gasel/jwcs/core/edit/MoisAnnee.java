package nc.ccas.gasel.jwcs.core.edit;

import java.util.Calendar;
import java.util.Date;

import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.psm.MoisPSM;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.PropertySelection;

public abstract class MoisAnnee extends AbstractFormComponent {

	@Parameter(required = true)
	public abstract Date getMois();

	public abstract void setMois(Date mois);

	@Parameter(name = "onchange")
	public abstract String getOnChange();

	@Parameter(name = "min")
	public abstract String getAnneeMin();

	@Parameter(name = "max")
	public abstract String getAnneeMax();

	@Component(bindings = { "value=ognl:numMois", "model=ognl:moisPSM",
			"onchange=ognl:onChange" })
	public abstract PropertySelection getSelectMois();

	@Component(type = "core/edit/Annee", inheritInformalParameters = true, bindings = {
			"value=ognl:annee", "min=anneeMin", "max=anneeMax",
			"onchange=ognl:onChange" })
	public abstract Annee getAnneeEdit();

	public abstract int getNumMois();

	public abstract void setNumMois(int numMois);

	public abstract int getAnnee();

	public abstract void setAnnee(int annee);

	@Override
	protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		setNumMois(getDefaultMois());
		setAnnee(getDefaultAnnee());

		getSelectMois().render(writer, cycle);
		getAnneeEdit().render(writer, cycle);
	}

	@Override
	protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		renderFormComponent(writer, cycle);
		setMois(DateUtils.debutMois(getAnnee(), getNumMois() - 1));
	}

	public int getDefaultMois() {
		// MoisPSM démarre à 1
		return DateUtils.dateField(getMois(), Calendar.MONTH) + 1;
	}

	public int getDefaultAnnee() {
		return DateUtils.dateField(getMois(), Calendar.YEAR);
	}

	public IPropertySelectionModel getMoisPSM() {
		return MoisPSM.INSTANCE;
	}

}
