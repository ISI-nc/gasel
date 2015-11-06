package nc.ccas.gasel.jwcs.core;

import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.link.ServiceLink;

public abstract class ServiceButton extends AbstractLinkButton {

	@Parameter(required = true)
	public abstract String getService();

	@Parameter
	public abstract Object getParameters();

	@Component(bindings = { "service=service", "parameters=parameters",
			"id=clientId" })
	public abstract ServiceLink getLink();
	
	@Override
	protected ILinkComponent getLinkImpl() {
		return getLink();
	}

}
