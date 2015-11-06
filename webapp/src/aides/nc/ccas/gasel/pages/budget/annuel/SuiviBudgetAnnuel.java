package nc.ccas.gasel.pages.budget.annuel;

import java.util.Date;
import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.agents.budget.ResumeBudgetaire;
import nc.ccas.gasel.agents.budget.SuiviAnnuel;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.tapestry.IRequestCycle;

public abstract class SuiviBudgetAnnuel extends BasePage implements
		PeriodeProps {

	private List<ResumeBudgetaire> _tableau;

	public List<ResumeBudgetaire> getTableau() {
		if (_tableau == null) {
			_tableau = SuiviAnnuel.INSTANCE.getSuivi(sql, //
					getPeriodeDebut(), getPeriodeFin());
		}
		return _tableau;
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_tableau = null;
	}

	public abstract Double getMontantBudgetAnnuel();

	public abstract Imputation getImputation();

	public abstract Short getAnnee();

	public Date getDefaultPeriodeDebut() {
		return DateUtils.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return DateUtils.finAnnee();
	}

}
