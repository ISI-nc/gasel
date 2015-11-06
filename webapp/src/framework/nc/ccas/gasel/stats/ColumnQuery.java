package nc.ccas.gasel.stats;

import java.util.List;

public class ColumnQuery {

	private List<ColumnSelect> selects;

	private ColumnSelect currentSelect;
	
	public ColumnQuery() {
		union();
	}
	
	public ColumnQuery(ColumnSelect select) {
		union(select);
	}

	public ColumnQuery union() {
		return union(new ColumnSelect());
	}

	public ColumnQuery union(ColumnSelect select) {
		currentSelect = select;
		selects.add(currentSelect);
		return this;
	}

	public ColumnSelect getCurrentSelect() {
		return currentSelect;
	}

	public List<ColumnSelect> getSelects() {
		return selects;
	}

}
