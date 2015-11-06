package nc.ccas.gasel;

import nc.ccas.gasel.workflow.Workflow;
import nc.ccas.gasel.workflow.WorkflowEntry;

import org.apache.tapestry.IPage;

public class BasePageWorkflow {

	private final BasePage page;

	public BasePageWorkflow(BasePage page) {
		this.page = page;
	}

	/**
	 * Recherche une valeur dans les parents (parcours du workflow).
	 */
	public Object componentValue(Class<?> pageClass, String componentPath,
			String propertyName) {
		Workflow wf = page.getWorkflow();
		WorkflowEntry originalEntry = wf.getCurrentEntry();
		for (WorkflowEntry entry = originalEntry.getParent(); entry != null; //
		entry = entry.getParent()) {
			IPage p = page.getRequestCycle().getPage(entry.getPageName());
			if (!pageClass.isInstance(p))
				continue;
			return entry.valueFor(componentPath, propertyName);
		}
		return null;
	}

}
