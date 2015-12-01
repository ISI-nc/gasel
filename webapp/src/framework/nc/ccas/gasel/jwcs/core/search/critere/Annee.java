package nc.ccas.gasel.jwcs.core.search.critere;

import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.JANUARY;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import nc.ccas.gasel.jwcs.core.search.Critere;
import nc.ccas.gasel.psm.NumberPSM;

import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.PropertySelection;

import com.asystan.common.cayenne.QueryFactory;

public abstract class Annee extends Critere {

	@Parameter(defaultValue = "ognl:anneeActuelle - 10")
	public abstract int getMin();

	@Parameter(defaultValue = "ognl:anneeActuelle")
	public abstract int getMax();

	@Persist("workflow")
	public abstract Integer getAnnee();

	public abstract void setAnnee(Integer annee);

	@Component(type = "PropertySelection", bindings = { "value=ognl:annee",
			"model=ognl:modeleAnnee" })
	public abstract PropertySelection getAnneeSelect();

	@Override
	public Expression buildExpressionImpl(String path) {
		Integer annee = getAnnee();
		if (annee == null) {
			return null;
		}
		Date debut = new GregorianCalendar(annee, JANUARY, 1).getTime();
		Date fin = new GregorianCalendar(annee, DECEMBER, 31, 23, 59, 59)
				.getTime();
		return QueryFactory.createBetween(path, debut, fin);
	}

	@Override
	protected void renderImpl(IMarkupWriter writer, IRequestCycle cycle) {
		getAnneeSelect().render(writer, cycle);
	}

	public IPropertySelectionModel getModeleAnnee() {
		return new NumberPSM(getMin(), getMax());
	}

	public int getAnneeActuelle() {
		return new GregorianCalendar().get(Calendar.YEAR);
	}

}
