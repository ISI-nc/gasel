package nc.ccas.gasel.jwcs.core.tableau;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.Parameter;

public abstract class ActionDetails extends AbstractAction {

	@Parameter(required = true)
	public abstract IActionListener getAction();

	@Parameter(defaultValue = "ognl:loupe")
	public abstract IAsset getIcon();

	@Parameter(defaultValue = "literal:Détails")
	public abstract Object getTitle();

	@Parameter(defaultValue = "false")
	public abstract Object getConfirm();

	@Parameter(defaultValue = "false")
	public abstract boolean getDisabled();

	@Asset("context:/images/loupe.gif")
	public abstract IAsset getLoupe();

}
