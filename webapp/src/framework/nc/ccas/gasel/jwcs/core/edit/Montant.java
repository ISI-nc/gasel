package nc.ccas.gasel.jwcs.core.edit;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.TextField;

public abstract class Montant extends AbstractComponent {

	@Parameter
	public abstract Number getValue();

	@Parameter
	public abstract boolean isDisabled();

	@Component(bindings = { "value=value", "disabled=disabled",
			"translator=translator:reel" }, inheritInformalParameters = true)
	public abstract TextField getField();

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		getField().render(writer, cycle);
	}

}
