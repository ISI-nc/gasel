package nc.ccas.gasel.jwcs.core.tableau;

import java.io.Serializable;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.Parameter;

public abstract class Action extends AbstractAction implements Serializable {
	private static final long serialVersionUID = -4986620977856983553L;

	@Parameter(required = true)
	public abstract IActionListener getAction();

	@Parameter
	public abstract IAsset getIcon();

	@Parameter
	public abstract Object getTitle();
	
	@Parameter(defaultValue = "false")
	public abstract Object getConfirm();
	
	@Parameter(defaultValue = "false")
	public abstract boolean getDisabled();

}
