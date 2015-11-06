package nc.ccas.gasel.jwcs;

import nc.ccas.gasel.BaseComponent;

public abstract class BorderDevAdds extends BaseComponent {

	public abstract String getPageName();

	public void workflowOpen() {
		System.out.println("====> MANUAL WORKFLOW OPEN: " + getPageName());
		getPage().workflowOpen(getPageName());
	}

	public void exceptionTest() {
		throw new RuntimeException("Test d'exception");
	}

}
