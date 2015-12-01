package nc.ccas.gasel.stats;

import java.io.Serializable;

public class FactCell implements Serializable {
	private static final long serialVersionUID = -6076610084933677669L;

	private final String value;

	private final Integer count;

	private boolean dottedBorder = false;

	public FactCell(String value, Integer count) {
		this.value = value;
		this.count = count;
	}

	public String inspect() {
		return "<FactCell: \"" + value + "\""
				+ (count == null ? "" : "x" + count) + ">";
	}

	@Override
	public String toString() {
		if (value == null)
			return "";
		return value + (count == null ? "" : ": " + count);
	}

	public Integer getCount() {
		return count;
	}

	public String getValue() {
		return value;
	}

	public boolean isDottedBorder() {
		return dottedBorder;
	}

	public void setDottedBorder(boolean dottedBorder) {
		this.dottedBorder = dottedBorder;
	}

}
