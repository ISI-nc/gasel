package nc.ccas.gasel.jwcs.core.tableau;

import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.ComplexDeletion;

import org.apache.cayenne.DataObject;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Parameter;

public abstract class ActionSupprimer extends AbstractAction {

	@Override
	@InitialValue("listener:delete")
	public abstract IActionListener getAction();

	@Parameter
	public abstract DataObject getParent();

	public abstract void setParent(DataObject parent);

	@Parameter
	public abstract String getListe();

	@Override
	@Parameter(defaultValue = "ognl:delete")
	public abstract IAsset getIcon();

	@Override
	@Parameter(defaultValue = "literal:Supprimer")
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
		if (getParent() != null) {
			if (getListe() == null) {
				throw new RuntimeException("Vous devez donner un nom de"
						+ " liste si vous donnez un parent");
			}
			return new Object[] { getParent() };
		}
		return super.getParameters();
	}

	public <T extends DataObject> void delete(IRequestCycle cycle, T value,
			DataObject parent) {
		setParent(parent);
		delete(cycle, value);
	}

	public <T extends DataObject> void delete(IRequestCycle cycle, T value) {
		if (getParent() != null) {
			List<?> liste = (List<?>) PropertyUtils.read(getParent(), getListe());
			liste.remove(value);
		}
		if (value instanceof ComplexDeletion) {
			((ComplexDeletion) value).prepareForDeletion();
		}
		value.getObjectContext().deleteObject(value);
		if (getParent() == null) {
			value.getObjectContext().commitChanges();
		}

		IActionListener afterDelete = getAfterDelete();
		if (afterDelete != null) {
			afterDelete.actionTriggered(this, cycle);
		}
		((BasePage) getPage()).redirect();
	}
}
