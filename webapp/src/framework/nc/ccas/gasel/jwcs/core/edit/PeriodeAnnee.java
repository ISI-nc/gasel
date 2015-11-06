package nc.ccas.gasel.jwcs.core.edit;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.AbstractFormComponent;

@ComponentClass(allowBody = false)
public abstract class PeriodeAnnee extends AbstractFormComponent {

	@Parameter(required = true)
	public abstract Date getDebut();

	public abstract void setDebut(Date date);

	@Parameter(required = true)
	public abstract Date getFin();

	public abstract void setFin(Date date);

	@Parameter(name = "min")
	public abstract Integer getAnneeMin();

	@Parameter(name = "max")
	public abstract Integer getAnneeMax();

	@Parameter(name = "onchange")
	public abstract String getOnChange();

	@Component(type = "core/edit/Annee", inheritInformalParameters = true, bindings = {
			"value=ognl:annee", "min=anneeMin", "max=anneeMax",
			"onchange=ognl:onChange" })
	public abstract Annee getAnneeEdit();

	@Override
	protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		getAnneeEdit().render(writer, cycle);
	}

	@Override
	protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		renderFormComponent(writer, cycle);
	}

	/* Propriété calculé "annee" */

	public Number getAnnee() {
		GregorianCalendar gc = new GregorianCalendar();
		if (getDebut() != null) {
			gc.setTime(getDebut());
		}
		return gc.get(Calendar.YEAR);
	}

	public void setAnnee(Number anneeNb) {
		if (anneeNb == null) {
			return;
		}
		int annee = anneeNb.intValue();
		if (annee < 100) {
			if (annee > 50) {
				annee += 1900;
			} else {
				annee += 2000;
			}
		}
		setDebut(new GregorianCalendar(annee, Calendar.JANUARY, 1).getTime());
		setFin(new GregorianCalendar(annee, Calendar.DECEMBER, 31, 23, 59, 59)
				.getTime());
	}

}
