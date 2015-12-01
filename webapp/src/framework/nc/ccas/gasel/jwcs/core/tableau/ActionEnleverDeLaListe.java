package nc.ccas.gasel.jwcs.core.tableau;

import nc.ccas.gasel.BasePage;

import org.apache.cayenne.DataObject;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Parameter;

public abstract class ActionEnleverDeLaListe extends AbstractAction {

	@Override
	@InitialValue("listener:delete")
	public abstract IActionListener getAction();

	@Parameter(required = true)
	public abstract DataObject getParent();

	public abstract void setParent(DataObject parent);

	@Parameter(required = true)
	public abstract String getListe();

	@Override
	@Parameter(defaultValue = "ognl:delete")
	public abstract IAsset getIcon();

	@Override
	@Parameter(defaultValue = "literal:Supprimer de la liste")
	public abstract Object getTitle();

	@Override
	@Parameter(defaultValue = "true")
	public abstract Object getConfirm();

	@Override
	@Parameter(defaultValue = "false")
	public abstract boolean getDisabled();

	@Parameter
	public abstract IActionListener getAfterDelete();

	@Asset("context:/images/delete.gif")
	public abstract IAsset getDelete();

	@Override
	public Object[] getParameters() {
		return new Object[] { getParent() };
	}

	public <T extends DataObject> void delete(IRequestCycle cycle, T value,
			DataObject parent) {
		setParent(parent);
		delete(cycle, value);
	}

	public <T extends DataObject> void delete(IRequestCycle cycle, T value) {
		getParent().removeToManyTarget(getListe(), value, true);
		((BasePage) getPage()).redirect();
	}
}
