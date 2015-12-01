package nc.ccas.gasel.workflow;

import org.apache.tapestry.engine.state.StateObjectFactory;

public class WorkflowStateObjectFactory implements StateObjectFactory {

	private String pageName;

	private String title;

	public Object createStateObject() {
		return new Workflow(pageName, title);
	}

	public void setPageName(String workflowRootPageName) {
		this.pageName = workflowRootPageName;
	}

	public void setTitle(String workflowRootTitle) {
		this.title = workflowRootTitle;
	}

}
