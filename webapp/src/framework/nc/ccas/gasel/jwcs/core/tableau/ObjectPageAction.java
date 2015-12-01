package nc.ccas.gasel.jwcs.core.tableau;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.ObjectCallback;
import nc.ccas.gasel.ObjectPage;

import org.apache.cayenne.DataObject;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Parameter;

public abstract class ObjectPageAction extends AbstractAction {

	@Override
	@Parameter(defaultValue = "ognl:loupe")
	public abstract IAsset getIcon();

	@Override
	@Parameter(defaultValue = "literal:Voir")
	public abstract Object getTitle();

	@Override
	@Parameter(defaultValue = "false")
	public abstract Object getConfirm();

	@Override
	@Parameter(defaultValue = "false")
	public abstract boolean getDisabled();

	@Parameter(required = true, name = "page")
	public abstract String getPageName();

	@Parameter
	public abstract <T, P extends BasePage> ObjectCallback<P, T> getCallback();

	@Parameter
	public abstract String getParentProperty();

	@Parameter
	public abstract Object getValue();

	@Override
	@InitialValue("listener:open")
	public abstract IActionListener getAction();

	@Asset("context:/images/loupe.gif")
	public abstract IAsset getLoupe();

	@SuppressWarnings("unchecked")
	public <T extends DataObject> void open(IRequestCycle cycle, Object value) {
		if (getValue() != null) {
			value = getValue(); // même pas vrai...
		}
		ObjectPage<T> page = (ObjectPage<T>) cycle.getPage(getPageName());
		if (getParentProperty() != null) {
			((EditPage<T>) page).openWithParent((T) value, getParentProperty(),
					getCallback());
		} else {
			page.open((T) value, getCallback());
		}
	}

}
