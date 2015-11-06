package nc.ccas.gasel.jwcs.core;

import nc.ccas.gasel.workflow.WorkflowDisplayHelper;
import nc.ccas.gasel.workflow.WorkflowEntry;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectState;

public abstract class Workflow extends BaseComponent {

	@InjectState("workflow")
	public abstract nc.ccas.gasel.workflow.Workflow getWorkflow();

	public abstract WorkflowEntry getCol();

	public abstract int getX();

	public abstract int getY();

	private WorkflowEntry[][] _entries;

	public void open(IRequestCycle cycle, int id) {
		getWorkflow().get(id).redirect(cycle);
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_entries = null;
	}

	public IAsset getFleche() {
		int x = getX();
		int y = getY();
		String cards = WorkflowDisplayHelper.INSTANCE //
				.cardinalities(x, y, getRows());

		if (cards.length() == 0) {
			return null;
		}

		IAsset fleche = getAsset(cards.toString());
		if (fleche == null) {
			System.err.println("[XXX] Fleche invalide : " + cards.toString()
					+ " (" + x + "," + y + ")");
		}
		return fleche;
	}

	public WorkflowEntry[][] getRows() {
		if (_entries == null) {
			_entries = WorkflowDisplayHelper.INSTANCE.toArray(getWorkflow());
		}
		return _entries;
	}

}