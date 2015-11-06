package nc.ccas.gasel;

import nc.ccas.gasel.workflow.Workflow;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.Persistent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectState;

public abstract class AbstractComponent extends
		org.apache.tapestry.AbstractComponent {

	@InjectState("workflow")
	public abstract Workflow getWorkflow();

	public void reloadPage(IRequestCycle cycle) {
		getWorkflow().redirect(cycle);
	}

	public void reloadPage() {
		reloadPage(getPage().getRequestCycle());
	}

	// ------------------------------------------------------------------
	// Submit listeners support
	//

	public void registerOnSubmit(String methodName) {
		if (!getListeners().canProvideListener(methodName))
			throw new IllegalArgumentException("No listener for " + methodName);

		page().registerOnSubmit(getListeners().getListener(methodName));
	}

	public ObjectContext getObjectContext() {
		return DataContext.getThreadDataContext();
		// return (page()).getObjectContext();
	}

	public <T extends Persistent> T newObject(Class<T> clazz) {
		return clazz.cast(getObjectContext().newObject(clazz));
	}

	/* Extensions de la page */

	public BasePageSql getSql() {
		return page().sql;
	}

	public BasePagePsm getPsm() {
		return page().psm;
	}

	public BasePageDates getDates() {
		return page().dates;
	}

	protected BasePage page() {
		return getPage();
	}

	@Override
	public BasePage getPage() {
		return (BasePage) super.getPage();
	}

	protected void log(Object message) {
		getPage().log(this, message);
	}

}
