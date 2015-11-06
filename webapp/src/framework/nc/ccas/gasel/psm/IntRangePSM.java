package nc.ccas.gasel.psm;

import org.apache.tapestry.form.IPropertySelectionModel;

public class IntRangePSM implements IPropertySelectionModel {

	private final int from;

	private final int to;

	private final int increment;

	public IntRangePSM(int from, int to) {
		this(from, to, from > to ? -1 : 1);
	}

	private IntRangePSM(int from, int to, int increment) {
		this.from = from;
		this.to = to;
		this.increment = increment;
	}

	public String getLabel(int index) {
		return getValue(index);
	}

	public Object getOption(int index) {
		return increment == -1 ? to - index : from + index;
	}

	public int getOptionCount() {
		return to - from + 1; // 10 à 10 => 1
	}

	public String getValue(int index) {
		Object opt = getOption(index);
		return opt == null ? "X" : opt.toString();
	}

	public Object translateValue(String value) {
		if ("X".equals(value)) {
			return null;
		}
		int retval = Integer.parseInt(value);
		if (retval < from || retval > to) {
			throw new RuntimeException("Invalid value: " + retval);
		}
		return retval;
	}

	public boolean isDisabled(int index) {
		return false;
	}

}
