package nc.ccas.gasel.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.asystan.common.StringUtils;

class ColNode {

	private final int colNum;

	private final List<ColumnDefinition> colDefs;

	private final ColumnDefinition colDef;

	private final Map<String, Integer> counts;

	// Pour le mode groupant
	private final ColNode child;

	// Pour le mode non groupant
	private final Map<String, ColNode> children;

	public ColNode(int colNum, List<ColumnDefinition> colDefs) {
		this.colNum = colNum;
		this.colDefs = colDefs;
		colDef = colDefs.get(colNum);

		if (colDef.isGrouping()) {
			child = newChild();
			children = null;
		} else {
			child = null;
			children = new HashMap<String, ColNode>();
		}

		if (colDef.isCounting() || colDef.isGrouping()) {
			counts = new HashMap<String, Integer>();
		} else {
			counts = null;
		}
	}

	public ColNode process(DataItem item) {
		List<Object> values = item.get(colNum);
		Set<String> reprs = colDef.getRepr().representation(values);
		// System.out.println("-[" + colNum + "]-\""+colDef.getTitre()+"\"-> " +
		// StringUtils.join("|", reprs));
		if (colDef.isGrouping()) {
			// System.out.println("-- grouping");
			for (String repr : reprs) {
				incCount(repr);
			}
			return child;
		} else if (!reprs.isEmpty()) {
			// System.out.println("-- non-grouping");
			String repr = StringUtils.join(", ", reprs);
			if (colDef.isCounting()) {
				incCount(repr);
			}
			if (!children.containsKey(repr)) {
				children.put(repr, newChild());
			}
			return children.get(repr);
		} else {
			return null;
		}
	}

	public List<FactLine> getLines() {
		if (!colDef.isGrouping()) {
			List<FactLine> retval = new ArrayList<FactLine>();
			for (String key : orderedKeys(children)) {
				ColNode node = children.get(key);
				Integer count = countOf(key);
				FactCell cell = new FactCell(key, count);
				retval.add(new FactLine(Arrays.asList(cell),
						node == null ? null : node.getLines(), numColsAfter()));
			}
			return retval;

		} else {
			List<FactCell> cells = new ArrayList<FactCell>();
			for (String key : orderedKeys(counts)) {
				Integer count = countOf(key);
				cells.add(new FactCell(key, count));
			}
			List<FactLine> childLines = new ArrayList<FactLine>();
			if (child == null) {
				childLines = null;
			} else {
				childLines = child.getLines();
			}
			return Arrays
					.asList(new FactLine(cells, childLines, numColsAfter()));
		}
	}

	private Integer countOf(String key) {
		return colDef.isCounting() ? counts.get(key) : null;
	}

	private List<String> orderedKeys(Map<String, ?> map) {
		ArrayList<String> keys = new ArrayList<String>(map.size());
		keys.addAll(map.keySet());
		Collections.sort(keys, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		return keys;
	}

	private int numColsAfter() {
		return colDefs.size() - colNum - 1;
	}

	// ------------------------------------------------------------------

	private ColNode newChild() {
		if (colNum + 1 < colDefs.size()) {
			return new ColNode(colNum + 1, colDefs);
		} else {
			return null;
		}
	}

	private void incCount(String element) {
		int val;
		if (counts.containsKey(element)) {
			val = counts.get(element);
		} else {
			val = 0;
		}
		counts.put(element, val + 1);
	}

}
