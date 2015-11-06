package nc.ccas.gasel.workflow;

public interface WorkflowDisplayHelper {

	public static final WorkflowDisplayHelperImpl INSTANCE = new WorkflowDisplayHelperImpl();

	public WorkflowEntry[][] toArray(Workflow workflow);

	public WorkflowEntry[][] toArray(WorkflowEntry entry);

	/**
	 * Compute the cardinalities of the arrow to put before the given entry. May
	 * be empty but not null.
	 * 
	 * @param x
	 *            Column of the entry.
	 * @param y
	 *            Row of the entry.
	 * @param entries
	 *            The entry array where we are.
	 * @return The cardinalities of the arrow to put before the given entry. May
	 *         be empty but not null.
	 */
	public String cardinalities(int x, int y, WorkflowEntry[][] entries);

}
