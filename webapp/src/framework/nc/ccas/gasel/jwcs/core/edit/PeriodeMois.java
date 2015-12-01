package nc.ccas.gasel.jwcs.core.edit;

import java.util.Calendar;
import java.util.Date;

import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.psm.MoisPSM;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.PropertySelection;

@ComponentClass(allowBody = false)
public abstract class PeriodeMois extends AbstractFormComponent {

	@Parameter(required = true)
	public abstract Date getDebut();

	public abstract void setDebut(Date date);

	@Parameter(required = true)
	public abstract Date getFin();

	public abstract void setFin(Date date);

	@Parameter(defaultValue = "ognl:false")
	public abstract boolean getMemeAnnee();

	@Parameter(name = "min")
	public abstract String getAnneeMin();

	@Parameter(name = "max")
	public abstract String getAnneeMax();

	@Parameter(name = "onchange")
	public abstract String getOnChange();

	@Parameter(defaultValue = "5")
	public abstract Integer getSize();

	@Component(bindings = { "value=ognl:moisDebut", "model=ognl:moisPSM",
			"onchange=ognl:onChange" })
	public abstract PropertySelection getSelectMoisDebut();

	@Component(bindings = { "value=ognl:moisFin", "model=ognl:moisPSM",
			"onchange=ognl:onChange" })
	public abstract PropertySelection getSelectMoisFin();

	@Component(type = "core/edit/Annee", inheritInformalParameters = true, bindings = {
			"value=ognl:annee", "min=anneeMin", "max=anneeMax",
			"onchange=ognl:onChange", "style='width:'+size+'em;'" })
	public abstract Annee getAnneeEdit();

	@Component(type = "core/edit/Annee", inheritInformalParameters = true, bindings = {
			"value=ognl:anneeDebut", "min=anneeMin", "max=anneeMax",
			"onchange=ognl:onChange", "style='width:'+size+'em;'" })
	public abstract Annee getAnneeDebutEdit();

	@Override
	protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		setMoisDebut(getDefaultMoisDebut());
		setMoisFin(getDefaultMoisFin());
		setAnneeDebut(getDefaultAnneeDebut());
		setAnnee(getDefaultAnneeFin());

		getSelectMoisDebut().render(writer, cycle);
		if (!getMemeAnnee()) {
			getAnneeDebutEdit().render(writer, cycle);
		}
		writer.print(" à ");
		getSelectMoisFin().render(writer, cycle);
		getAnneeEdit().render(writer, cycle);
	}

	@Override
	protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		renderFormComponent(writer, cycle);
		// Définition des dates
		int debMois = getMoisDebut() - 1;
		int debAnnee = getMemeAnnee() ? getAnnee() : getAnneeDebut();
		updateDebut(debAnnee, debMois);

		int finMois = getMoisFin() - 1;
		int finAnnee = getAnnee();
		updateFin(finAnnee, finMois);
	}

	public IPropertySelectionModel getMoisPSM() {
		return MoisPSM.INSTANCE;
	}

	/* debut */

	public abstract int getMoisDebut();

	public abstract void setMoisDebut(int value);

	public abstract int getAnneeDebut();

	public abstract void setAnneeDebut(int value);

	public int getDefaultMoisDebut() {
		// MoisPSM démarre à 1
		return DateUtils.dateField(getDebut(), Calendar.MONTH) + 1;
	}

	public int getDefaultAnneeDebut() {
		return DateUtils.dateField(getDebut(), Calendar.YEAR);
	}

	private void updateDebut(int annee, int mois) {
		setDebut(DateUtils.mois(annee, mois)[0]);
	}

	/* fin */

	public abstract int getMoisFin();

	public abstract void setMoisFin(int value);

	public abstract int getAnnee();

	public abstract void setAnnee(int value);

	public int getDefaultMoisFin() {
		// MoisPSM démarre à 1
		return DateUtils.dateField(getFin(), Calendar.MONTH) + 1;
	}

	public int getDefaultAnneeFin() {
		return DateUtils.dateField(getFin(), Calendar.YEAR);
	}

	private void updateFin(int annee, int mois) {
		setFin(DateUtils.mois(annee, mois)[1]);
	}

}
