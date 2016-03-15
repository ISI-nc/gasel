package nc.ccas.gasel.jwcs.core.search.critere;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import nc.ccas.gasel.jwcs.core.search.Critere;
import nc.ccas.gasel.psm.MoisPSM;

import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.PropertySelection;
import org.apache.tapestry.form.TextField;

import com.asystan.common.cayenne_new.QueryFactory;

public abstract class MoisAnnee extends Critere {

	@Persist("workflow")
	public abstract Integer getMois();

	public abstract void setMois(Integer mois);

	@Persist("workflow")
	public abstract Number getAnnee();

	public abstract void setAnnee(Number annee);

	@Component(type = "PropertySelection", bindings = { "value=ognl:mois",
			"model=ognl:modeleMois" })
	public abstract PropertySelection getMoisSelect();

	@Component(type = "TextField", bindings = { "value=ognl:annee",
			"translator=translator:number,omitZero", "size=literal:4",
			"displayName=literal:Année" })
	public abstract TextField getAnneeField();

	public Date getDateMois() {
		if (getAnnee() == null || getAnnee().intValue() == 0) {
			return null;
		}
		return dateMois().getTime();
	}

	@Override
	public Expression buildExpressionImpl(String path) {
		if (getAnnee() == null || getAnnee().intValue() == 0) {
			return null;
		}
		GregorianCalendar gc = dateMois();
		Date debut = gc.getTime();
		gc.add(Calendar.MONTH, 1);
		gc.add(Calendar.SECOND, -1);
		Date fin = gc.getTime();
		return QueryFactory.createBetween(path, debut, fin);
	}

	private GregorianCalendar dateMois() {
		return new GregorianCalendar(getAnnee().intValue(), getMois() - 1, 1);
	}

	@Override
	protected void renderImpl(IMarkupWriter writer, IRequestCycle cycle) {
		getMoisSelect().render(writer, cycle);
		writer.print(" ");
		getAnneeField().render(writer, cycle);
	}

	public IPropertySelectionModel getModeleMois() {
		return MoisPSM.INSTANCE;
	}

}
