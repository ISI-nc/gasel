package nc.ccas.gasel.pages.budget.annuel;

import nc.ccas.gasel.BasePage;

public abstract class HANombre extends BasePage implements HAPage {

	public String agregation() {
		return "COUNT(*)";
	}

}
