package nc.ccas.gasel.workflow;

import java.util.ArrayList;
import java.util.List;

public class WorkflowDisplayHelperImpl implements WorkflowDisplayHelper {

	public WorkflowEntry[][] toArray(Workflow workflow) {
		return toArray(workflow.getRoot());
	}

	public WorkflowEntry[][] toArray(WorkflowEntry entry) {
		List<EntryPosition> tableau = new ArrayList<EntryPosition>();
		computePositions(entry, new ComputePositionState(), tableau);

		int maxRow = 0;
		int maxCol = 0;
		for (EntryPosition pos : tableau) {
			if (maxRow < pos.row) {
				maxRow = pos.row;
			}
			if (maxCol < pos.col) {
				maxCol = pos.col;
			}
		}

		WorkflowEntry[][] array = new WorkflowEntry[maxRow + 1][maxCol + 1];
		for (EntryPosition pos : tableau) {
			array[pos.row][pos.col] = pos.entry;
		}
		return array;
	}

	private void computePositions(WorkflowEntry entry,
			ComputePositionState state, List<EntryPosition> positions) {
		positions.add(new EntryPosition(entry, state.row, state.col));
		// Descente
		state.goneUp = false;
		state.col++;
		// --
		for (WorkflowEntry child : entry.getChildren()) {
			if (state.goneUp) {
				state.row++;
			}
			computePositions(child, state, positions);
		}
		// Remontée
		state.goneUp = true;
		state.col--;
	}

	static class EntryPosition {
		final WorkflowEntry entry;

		int row, col;

		EntryPosition(WorkflowEntry entry, int row, int col) {
			this.entry = entry;
			this.row = row;
			this.col = col;
		}
	}

	static class ComputePositionState {

		boolean goneUp = false;

		int row = 0;

		int col = 0;

	}

	// ------------------------------------------------------------------------

	public String cardinalities(int x, int y, WorkflowEntry[][] entries) {
		StringBuilder buf = new StringBuilder();

		// Entrées dessus/dessous
		WorkflowEntry current = entries[y][x];
		WorkflowEntry prev = findEntry(x, y - 1, -1, entries);
		WorkflowEntry prevOrCurrent = current == null ? prev : current;
		WorkflowEntry next = findEntry(x, y + 1, 1, entries);
		WorkflowEntry nextOrCurrent = current == null ? next : current;

		// Nord
		if (prev != null && nextOrCurrent != null
				&& prev.getParent() == nextOrCurrent.getParent())
			buf.append('n');
		// Ouest
		if (x > 0 && entries[y][x - 1] != null)
			buf.append('o');
		// Est
		if (entries[y][x] != null)
			buf.append('e');
		// Sud
		if (prevOrCurrent != null && next != null
				&& prevOrCurrent.getParent() == next.getParent())
			buf.append('s');
		return buf.toString();
	}

	private WorkflowEntry findEntry(int x, int y, int increment,
			WorkflowEntry[][] entries) {
		for (int cy = y; cy >= 0 && cy < entries.length; cy += increment) {
			if (entries[cy][x] != null) {
				return entries[cy][x];
			}
		}
		return null;
	}

}
