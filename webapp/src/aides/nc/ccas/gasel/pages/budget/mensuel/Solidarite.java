package nc.ccas.gasel.pages.budget.mensuel;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.budget.Imputation;

import org.apache.tapestry.event.PageEvent;

public abstract class Solidarite extends BasePage {

	@Override
	public void pageBeginRender(PageEvent event) {
		Alimentation p = (Alimentation) getRequestCycle().getPage(
				"budget/mensuel/Alimentation");
		p.setImputationById(Imputation.SOLIDARITE);
		redirectTo(p);
	}

}
