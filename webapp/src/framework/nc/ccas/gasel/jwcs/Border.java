package nc.ccas.gasel.jwcs;

import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.pages.Query;
import nc.ccas.gasel.workflow.Workflow;
import nc.ccas.gasel.workflow.WorkflowEntry;

import org.apache.tapestry.annotations.InjectComponent;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.form.Form;

public abstract class Border extends BaseComponent {

	@InjectPage("Query")
	public abstract Query getQueryPage();

	public void openQuery(String query) {
		getQueryPage().open(query);
	}

	public void openQueryPage() {
		getQueryPage().open("SELECT * FROM ?");
	}

	public void cleanContext() {
		getObjectContext().rollbackChanges();
	}

	@InjectComponent("form")
	public abstract Form getForm();

	public void onSubmit() {
		page().invokeSubmitListeners();
	}

	/* Maintien de l'état du workflow */

	@Override
	@InjectState("workflow")
	public abstract Workflow getWorkflow();

	public int getWorkflowEntryId() {
		return getWorkflow().getCurrentEntry().getId();
	}

	public void setWorkflowEntryId(int entryId) {
		WorkflowEntry entry = getWorkflow().get(entryId);
		if (entry == null) {
			getWorkflow().redirect(getPage().getRequestCycle());
		}
		getWorkflow().setCurrentEntry(entry);
	}

}
