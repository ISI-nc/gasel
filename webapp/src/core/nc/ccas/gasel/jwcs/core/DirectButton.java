package nc.ccas.gasel.jwcs.core;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.link.DirectLink;

public abstract class DirectButton extends AbstractLinkButton {

	@Parameter(required = true)
	public abstract IActionListener getListener();

	@Parameter
	public abstract Object getParameters();

	@Component(bindings = { "listener=listener", "parameters=parameters",
			"id=clientId" })
	public abstract DirectLink getLink();

	@Override
	protected ILinkComponent getLinkImpl() {
		return getLink();
	}

}
