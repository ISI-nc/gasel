package nc.ccas.gasel.model.budget;

import static org.apache.cayenne.DataObjectUtils.intPKForObject;
import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.budget.auto._LigneVirement;

@Feminin
public class LigneVirement extends _LigneVirement {
	private static final long serialVersionUID = -1303572934459171042L;

	private final LigneVirementHelper helperSource = new LigneVirementHelper(
			this, true);

	private final LigneVirementHelper helperDestination = new LigneVirementHelper(
			this, false);

	public LigneVirementHelper getHelperSource() {
		return helperSource;
	}

	public LigneVirementHelper getHelperDestination() {
		return helperDestination;
	}

	public boolean isFrom(Budget budget) {
		if (getSourceId() == null || getSourceType() == null)
			return false;

		return getSourceId().intValue() == intPKForObject(budget)
				&& getSourceType().intValue() == budget.getType();
	}

	public boolean isTo(Budget budget) {
		if (getDestinationId() == null || getDestinationType() == null)
			return false;

		return getDestinationId().intValue() == intPKForObject(budget)
				&& getDestinationType().intValue() == budget.getType();
	}

}
