package nc.ccas.gasel.workflow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.cayenne.access.DataContext;
import org.apache.tapestry.IRequestCycle;

public class Workflow implements Serializable {
	private static final long serialVersionUID = -7528493361050848102L;

	private Map<Integer, WorkflowEntry> entries = new HashMap<Integer, WorkflowEntry>();

	private final WorkflowEntry root;

	private WorkflowEntry currentEntry;

	private int lastId = -1;

	public void activate(IRequestCycle cycle) {
		activationTarget().activate(cycle);
	}

	public void redirect(IRequestCycle cycle, String anchor) {
		activationTarget().redirect(cycle, anchor);
	}

	public void redirect(IRequestCycle cycle) {
		activationTarget().redirect(cycle);
	}

	private WorkflowEntry activationTarget() {
		return (currentEntry == null) ? root : currentEntry;
	}

	/**
	 * Ferme le workflow jusqu'au point actif.
	 * 
	 * @param cycle
	 *            Nécessaire pour activer/rediriger.
	 */
	public void close(IRequestCycle cycle) {
		close(getCurrentEntry(), cycle);
	}

	/**
	 * Ferme le workflow jusqu'à un point donné.
	 * 
	 * @param closePoint
	 *            Le point jusqu'où fermer le workflow.
	 * @param cycle
	 *            Nécessaire pour activer/rediriger.
	 */
	public void close(WorkflowEntry closePoint, IRequestCycle cycle) {
		close(closePoint, cycle, true);
	}

	/**
	 * Ferme le workflow jusqu'à un point donné.
	 * 
	 * @param closePoint
	 *            Le point jusqu'où fermer le workflow.
	 * @param cycle
	 *            Nécessaire pour activer/rediriger.
	 * @param redirect
	 *            <code>true</code> pour rediriger, <code>false</code> pour
	 *            simplement activer. L'activation/redirection n'a pas lieu si
	 *            <code>closePoint</code> n'est pas <code>currentEntry</code>
	 *            ou un des ses ancêtres.
	 */
	public void close(WorkflowEntry closePoint, IRequestCycle cycle,
			boolean redirect) {
		// <sanity>
		if (closePoint == root) {
			throw new IllegalArgumentException("Cannot remove workflow's root");
		}
		if (closePoint.getWorkflow() != this) {
			throw new IllegalArgumentException(
					"closePoint is not in this workflow");
		}
		// </sanity>
		boolean reparent = (currentEntry != null && closePoint
				.contains(currentEntry));
		for (WorkflowEntry entry : closePoint.descendants()) {
			removeEntry(entry);
		}
		removeEntry(closePoint);
		if (reparent) {
			setCurrentEntry(closePoint.getParent());
			if (redirect) {
				redirect(cycle);
			} else {
				activate(cycle);
			}
		}
	}

	private void removeEntry(WorkflowEntry entry) {
		WorkflowEntry parent = entry.getParent();
		if (parent != null) {
			parent.removeChild(entry);
		}
		entries.remove(entry.getId());
	}

	public Workflow(String pageName, String title) {
		root = currentEntry = new WorkflowEntry(pageName, title, this);
	}

	public WorkflowEntry get(int entryId) {
		return entries.get(entryId);
	}

	public WorkflowEntry getRoot() {
		return root;
	}

	protected synchronized int nextId() {
		lastId++;
		return lastId;
	}

	protected void register(WorkflowEntry entry) {
		entries.put(entry.getId(), entry);
	}

	public WorkflowEntry getCurrentEntry() {
		return currentEntry;
	}

	public void setCurrentEntry(WorkflowEntry currentEntry) {
		this.currentEntry = currentEntry;
		DataContext.bindThreadDataContext(currentEntry.getObjectContext());
	}

	public boolean getHasChildren() {
		return !getCurrentEntry().getChildren().isEmpty();
	}

}