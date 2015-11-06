package nc.ccas.gasel.pages;

import java.util.ArrayList;
import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.configuration.ModuleDescription;
import nc.ccas.gasel.configuration.ModulePart;
import nc.ccas.gasel.jwcs.core.SideBar;
import nc.ccas.gasel.services.ModuleLister;
import nc.ccas.gasel.services.reports.ReportService;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;

public abstract class Accueil extends BasePage {

	@Override
	public void workflowOpen(IRequestCycle cycle, String pageName) {
		getWorkflow().setCurrentEntry(getWorkflow().getRoot());
		super.workflowOpen(cycle, pageName);
	}

	@InjectObject("service:gasel.ModuleLister")
	public abstract ModuleLister getModuleLister();

	public List<SideBar.Category<SideBarValue>> getSideBar() {
		List<SideBar.Category<SideBarValue>> retval = new ArrayList<SideBar.Category<SideBarValue>>();

		for (ModuleDescription module : getModuleLister().getModules()) {
			SideBar.Category<SideBarValue> cat;
			cat = new SideBar.Category<SideBarValue>(module.getTitre());
			retval.add(cat);

			for (ModulePart part : module.getParts()) {
				cat.values.add(new SideBarValue(part));
			}
		}

		return retval;
	}

	public abstract SideBarValue getSel();

	public ModulePart getPart() {
		return getSel().part;
	}
	
	public void testReport(IRequestCycle cycle) {
		// XXX remove me
		ReportService.invoke(cycle, "test", null);
	}

	public static class SideBarValue extends SideBar.Value {
		private static final long serialVersionUID = 7360326747498914016L;
		
		public final ModulePart part;

		public SideBarValue(ModulePart part) {
			super(part.getTitre());
			this.part = part;
		}
	}

}
