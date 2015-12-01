package nc.ccas.gasel.stats;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class FactLine {

	private final List<FactCell> cells;

	private final List<FactLine> children;

	private final int numColsAfter;

	public FactLine(List<FactCell> cells, List<FactLine> children,
			int numColsAfter) {
		this.cells = cells;
		this.children = children;
		this.numColsAfter = numColsAfter;
	}

	public int getRowSpan() {
		if (children == null) {
			return cells.size();
		}
		int sum = 0;
		for (FactLine child : children) {
			sum += child.getRowSpan();
		}
		return Math.max(sum, cells.size());
	}

	public List<List<FactCell>> expand() {
		List<List<FactCell>> retval = new ArrayList<List<FactCell>>();
		Iterator<FactCell> cellIt = cells.iterator();
		boolean first = true;
		if (children != null) {
			for (FactLine child : children) {
				for (List<FactCell> childRow : child.expand()) {
					FactCell prefix = null;
					if (cellIt.hasNext()) {
						prefix = cellIt.next();
						prefix.setDottedBorder(!first);
						first = false;
					}
					childRow.add(0, prefix);
					retval.add(childRow);
				}
			}
		}
		while (cellIt.hasNext()) {
			List<FactCell> missingRow = new LinkedList<FactCell>();
			FactCell cell = cellIt.next();
			cell.setDottedBorder(!first);
			missingRow.add(cell);
			for (int i = 0; i < numColsAfter; i++) {
				FactCell emptyCell = new FactCell(null, null);
				emptyCell.setDottedBorder(!first);
				missingRow.add(emptyCell);
			}
			first = false;
			retval.add(missingRow);
		}
		return retval;
	}

	public List<FactCell> getCells() {
		return cells;
	}

	public List<FactLine> getChildren() {
		return children;
	}

}
