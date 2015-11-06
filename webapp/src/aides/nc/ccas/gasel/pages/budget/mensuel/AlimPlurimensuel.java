package nc.ccas.gasel.pages.budget.mensuel;

import static nc.ccas.gasel.pages.budget.mensuel.AlimOccImm.redirectMensuel;
import nc.ccas.gasel.BasePage;

import org.apache.tapestry.event.PageEvent;

public abstract class AlimPlurimensuel extends BasePage {

	@Override
	public void pageBeginRender(PageEvent event) {
		redirectMensuel("budget/annuel/AlimPlurimensuel", getRequestCycle());
	}

}
