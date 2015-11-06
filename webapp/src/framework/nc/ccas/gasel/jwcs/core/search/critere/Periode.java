package nc.ccas.gasel.jwcs.core.search.critere;

import java.util.Date;

import nc.ccas.gasel.jwcs.core.search.Critere;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.Checkbox;

import com.asystan.common.cayenne.QueryFactory;

public abstract class Periode extends Critere implements PeriodeProps {

	@Parameter(defaultValue = "ognl:false")
	public abstract boolean getToujoursActif();

	@Persist("workflow")
	public abstract boolean getActif();

	@Component(bindings = "value=ognl:actif")
	public abstract Checkbox getCheckbox();

	@Component(type = "core/edit/Periode", bindings = {
			"debut=ognl:periodeDebut", "fin=ognl:periodeFin" }, inheritInformalParameters = true)
	public abstract nc.ccas.gasel.jwcs.core.edit.Periode getPeriode();

	@Override
	protected void renderImpl(IMarkupWriter writer, IRequestCycle cycle) {
		if (!getToujoursActif()) {
			getCheckbox().render(writer, cycle);
		}
		getPeriode().render(writer, cycle);
	}

	@Override
	protected Expression buildExpressionImpl(String path) {
		if (!(getToujoursActif() || getActif())) {
			return null;
		}
		return QueryFactory.createBetween(path, getPeriodeDebut(),
				getPeriodeFin());
	}
	
	@Override
	public Date getDefaultPeriodeDebut() {
		return DateUtils.debutMois();
	}
	
	public Date getDefaultPeriodeFin() {
		return DateUtils.finMois();
	}

}