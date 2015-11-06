package nc.ccas.gasel.jwcs.core;

import nc.ccas.gasel.AbstractComponent;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.ILinkComponent;

public abstract class AbstractLinkButton extends AbstractComponent {

	@Parameter(required = true)
	public abstract String getLabel();

	@Parameter(defaultValue = "false")
	public abstract boolean getDisabled();

	protected abstract ILinkComponent getLinkImpl();

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		if (!getDisabled()) {
			writer.begin("div");
			writer.attribute("style", "display:none;");
			getLinkImpl().render(writer, cycle);
			writer.end(); // div
		}
		writer.begin("button");
		if (getDisabled()) {
			writer.attribute("disabled", "disabled");
		}
		writer.attribute("onclick", "popupLink('" + getClientId() + "');return false;");
		renderInformalParameters(writer, cycle);
		writer.print(getLabel());
		writer.end(); // button
	}

}
