package nc.ccas.gasel.jwcs.core.tableau;

import nc.ccas.gasel.ObjectCallbackPage;

import org.apache.cayenne.DataObject;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.Parameter;

public abstract class ActionChoisir extends AbstractAction {

	@Override
	@Parameter(defaultValue = "listener:select")
	public abstract IActionListener getAction();

	@Override
	@Parameter(defaultValue = "ognl:select")
	public abstract IAsset getIcon();

	@Override
	@Parameter(defaultValue = "literal:Choisir")
	public abstract Object getTitle();

	@Override
	@Parameter(defaultValue = "false")
	public abstract Object getConfirm();

	@Override
	@Parameter(defaultValue = "defaultDisabled")
	public abstract boolean getDisabled();

	@Asset("context:/images/select.gif")
	public abstract IAsset getSelect();

	public <T extends DataObject> void select(IRequestCycle cycle, T value) {
		page().success(value);
	}

	public boolean getDefaultDisabled() {
		return (page().getSuccessCallback() == null);
	}

	@SuppressWarnings("unchecked")
	private <T extends DataObject> ObjectCallbackPage<T> page() {
		return ((ObjectCallbackPage) getPage());
	}

}
