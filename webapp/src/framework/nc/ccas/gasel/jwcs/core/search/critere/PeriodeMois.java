package nc.ccas.gasel.jwcs.core.search.critere;

import java.util.Date;

import nc.ccas.gasel.BasePageDates;
import nc.ccas.gasel.jwcs.core.search.Critere;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.Checkbox;

import com.asystan.common.cayenne.QueryFactory;

public abstract class PeriodeMois extends Critere implements PeriodeProps {

	@Parameter(defaultValue = "ognl:false")
	public abstract boolean getToujoursActif();

	@Parameter(defaultValue = "ognl:true")
	public abstract boolean getMemeAnnee();

	@Persist("workflow")
	public abstract boolean getActif();

	@Component(bindings = "value=ognl:actif")
	public abstract Checkbox getCheckbox();

	@Component(type = "core/edit/PeriodeMois", bindings = {
			"debut=ognl:periodeDebut", "fin=ognl:periodeFin",
			"memeAnnee=ognl:memeAnnee" }, inheritInformalParameters = true)
	public abstract nc.ccas.gasel.jwcs.core.edit.PeriodeMois getPeriodeMois();

	public Date getDefaultPeriodeDebut() {
		return dates.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return dates.finAnnee();
	}

	@Override
	protected void renderImpl(IMarkupWriter writer, IRequestCycle cycle) {
		if (!getToujoursActif()) {
			getCheckbox().render(writer, cycle);
		}
		getPeriodeMois().render(writer, cycle);
	}

	@Override
	protected Expression buildExpressionImpl(String path) {
		if (!(getToujoursActif() || getActif())) {
			return null;
		}
		return QueryFactory.createBetween(path, getPeriodeDebut(),
				getPeriodeFin());
	}

	private BasePageDates dates = BasePageDates.INSTANCE;

}