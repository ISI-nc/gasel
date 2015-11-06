package nc.ccas.gasel.agents.budget;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.ValueFactory;
import nc.ccas.gasel.model.budget.BudgetAnnuel;
import nc.ccas.gasel.modelUtils.CommonQueries;

public class CachingBudgetInformator implements BudgetInformator {

	private static final String KEY_PREFIX = "__budget_informations__";

	public Integer getAnneeMin() {
		return BasePage.getRequestCache().get(new ValueFactory<Integer>() {
			public Integer buildValue() {
				return CommonQueries.min(BudgetAnnuel.class, "annee");
			}
		}, KEY_PREFIX, "anneeMin");
	}

	public Integer getAnneeMax() {
		return BasePage.getRequestCache().get(new ValueFactory<Integer>() {
			public Integer buildValue() {
				return CommonQueries.max(BudgetAnnuel.class, "annee");
			}
		}, KEY_PREFIX, "anneeMax");
	}

}
