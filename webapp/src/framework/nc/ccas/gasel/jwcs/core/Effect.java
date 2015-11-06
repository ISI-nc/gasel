package nc.ccas.gasel.jwcs.core;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;

public abstract class Effect extends AbstractComponent {

	@Parameter(required = true)
	public abstract IComponent getTarget();

	@Parameter
	public abstract String getEffect();

	@Parameter
	public abstract String getOptions();

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		writer.begin("script");
		writer.appendAttribute("type", "text/javascript");
		writer.printRaw("if (Element.Methods.visualEffect) {");
		writer.printRaw("document.getElementById('" + getTarget().getClientId()
				+ "').visualEffect('" + getEffect() + "'");
		if (getOptions() != null) {
			writer.printRaw(", '" + getOptions().replace("'", "\\'") + "'");
		}
		writer.printRaw(");}");
	}
}
