package nc.ccas.gasel.agents.budget;

public interface BudgetInformator {

	/**
	 * @return Première année de budget.
	 */
	public Integer getAnneeMin();

	/**
	 * @return Dernière année de budget.
	 */
	public Integer getAnneeMax();

	// TODO Hivemind
	public static final BudgetInformator INSTANCE = new CachingBudgetInformator();

}
