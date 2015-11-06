package nc.ccas.gasel.jwcs.bon;

import nc.ccas.gasel.jwcs.core.tableau.ActionSupprimer;
import nc.ccas.gasel.model.aides.UsageBon;

import org.apache.cayenne.DataObject;
import org.apache.tapestry.IRequestCycle;

public abstract class ActionSupprimerUsageBon extends ActionSupprimer {

	@Override
	public <T extends DataObject> void delete(IRequestCycle cycle, T value) {
		((UsageBon) value).getBon().setUsage(null);
		super.delete(cycle, value);
	}

}
