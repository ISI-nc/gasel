package nc.ccas.gasel.pages.budget.annuel;

import nc.ccas.gasel.BasePage;

public abstract class HAValeur extends BasePage implements HAPage {

	public String agregation() {
		return "SUM(montant_utilise)";
	}

}
