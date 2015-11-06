package nc.ccas.gasel.jwcs.parametres;

import org.apache.cayenne.DataObject;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IRequestCycle;

public abstract class ActionSupprimer extends
		nc.ccas.gasel.jwcs.core.tableau.ActionSupprimer {

	@Override
	public <T extends DataObject> void delete(IRequestCycle cycle, T value) {
		if (getParent() != null) {
			getParent().removeToManyTarget(getListe(), value, false);
		}
		PropertyUtils.write(value, "actif", false);
		value.getObjectContext().commitChanges();
	}

}
