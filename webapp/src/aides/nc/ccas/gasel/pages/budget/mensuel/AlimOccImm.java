package nc.ccas.gasel.pages.budget.mensuel;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;

public abstract class AlimOccImm extends BasePage {

	public static final void redirectMensuel(String pageName,
			IRequestCycle cycle) {
		BasePage bp = (BasePage) cycle.getPage(pageName);
		PeriodeProps pp = (PeriodeProps) bp;
		pp.setPeriodeDebut(DateUtils.debutMois());
		pp.setPeriodeFin(DateUtils.finMois());
		bp.redirectTo(bp);
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		redirectMensuel("budget/annuel/AlimOccImm", getRequestCycle());
	}

}
