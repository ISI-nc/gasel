package nc.ccas.gasel.pages.budget;

import java.util.Set;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.budget.BudgetAnnuel;

public abstract class Liste extends BasePage {
	

	public Set<BudgetAnnuel> getBudgetAnnuel() {
		return sql.query().all(BudgetAnnuel.class);
	}
	
	public abstract BudgetAnnuel getBudget();
	
}
