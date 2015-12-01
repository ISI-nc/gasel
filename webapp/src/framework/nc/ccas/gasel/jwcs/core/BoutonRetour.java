package nc.ccas.gasel.jwcs.core;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.workflow.Workflow;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.Submit;

public abstract class BoutonRetour extends AbstractComponent {

	@Component(type = "Submit", inheritInformalParameters = true, bindings = {
			"action=listener:retour", "class=cssClass", "value=value",
			"disabled=ognl:disabled or hasChildren", "id=clientId" })
	public abstract Submit getRetour();

	@Parameter(defaultValue = "literal:Retour")
	public abstract String getValue();

	@Parameter(name = "class", defaultValue = "literal:button")
	public abstract String getCssClass();

	@Parameter
	public abstract boolean getDisabled();

	public void retour(IRequestCycle cycle) {
		BasePage page = (BasePage) getPage();
		if (page.getWorkflow().getCurrentEntry() == page.getWorkflow()
				.getRoot()) {
			page.getWorkflow().redirect(cycle);
			return; // Impossible à fermer
		}
		page.workflowClose(cycle);
	}

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		getRetour().render(writer, cycle);
	}

	@InjectState("workflow")
	public abstract Workflow getWorkflow();

	public boolean getHasChildren() {
		return !getWorkflow().getCurrentEntry().getChildren().isEmpty();
	}

}
