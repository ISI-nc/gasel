package nc.ccas.gasel.jwcs.core.tableau;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;

public abstract class AbstractAction extends Block {

	public abstract IActionListener getAction();

	public abstract IAsset getIcon();

	public abstract Object getTitle();

	public abstract Object getConfirm();

	public abstract boolean getDisabled();

	public Object[] getParameters() {
		return new Object[] {};
	}

	@Override
	protected void prepareForRender(IRequestCycle cycle) {
		if (!(getConfirm() instanceof Boolean || getConfirm() instanceof String)) {
			throw new ApplicationRuntimeException(
					"Le paramètre 'confirm' doit être un booléen ou une chaîne.");
		}
	}

}
