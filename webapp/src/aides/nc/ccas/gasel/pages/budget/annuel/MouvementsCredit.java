package nc.ccas.gasel.pages.budget.annuel;

import java.util.Date;
import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.agents.budget.ResumeBudgetaire;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.budget.SecteurAide;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.tapestry.IRequestCycle;

public abstract class MouvementsCredit extends BasePage implements PeriodeProps {

	private List<ResumeBudgetaire> _tableau;

	public List<ResumeBudgetaire> getTableau() {
		if (_tableau == null) {
			_tableau = nc.ccas.gasel.agents.budget.MouvementsCredit.INSTANCE
					.getMouvementsCredit(sql, getPeriodeDebut(),
							getPeriodeFin());
		}
		return _tableau;
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_tableau = null;
	}

	public abstract Short getAnnee();

	public abstract Imputation getImputation();

	public abstract SecteurAide getSecteurAide();

	public Date getDefaultPeriodeDebut() {
		return DateUtils.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return DateUtils.finAnnee();
	}

}
