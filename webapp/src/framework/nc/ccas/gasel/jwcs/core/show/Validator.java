package nc.ccas.gasel.jwcs.core.show;

import java.util.Collection;

import nc.ccas.gasel.CheckResult;
import nc.ccas.gasel.French;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;

/**
 * @author Mikaël Cluseau - ISI.NC
 */
public abstract class Validator extends AbstractComponent {

	@Parameter(defaultValue = "ognl:page.validator")
	public abstract CheckResult getValidator();

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		if (getValidator().getErrors().isEmpty()
				&& getValidator().getWarnings().isEmpty()) {
			return;
		}

		writer.begin("div");
		writer.attribute("class", "form-errors");
		renderInformalParameters(writer, cycle);

		writeMessages(writer, "errors", getValidator().getErrors());
		writeMessages(writer, "warnings", getValidator().getWarnings());

		if (getValidator().getErrors().isEmpty()) {
			renderBody(writer, cycle);
		}

		writer.end(); // div
	}

	private void writeMessages(IMarkupWriter writer, String cssClass,
			Collection<String> messages) {
		if (messages.isEmpty()) {
			return;
		}
		writer.begin("div");
		writer.attribute("class", "expander-" + cssClass);
		writer.attribute("onclick", "toggleDisplay(nextSibling)");
		writer.print(messages.size() + " "
				+ French.pluriel(getMessages().getMessage(cssClass), //
						messages.size()));
		writer.end(); // div
		writer.begin("div");
		writer.attribute("style", "position:absolute;display:none;");
		writer.attribute("class", cssClass);
		writer.begin("ul");
		for (String message : messages) {
			writer.begin("li");
			writer.print(message);
			writer.end();
		}
		writer.end(); // ul
		writer.end(); // div
	}

}
