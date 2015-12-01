package nc.ccas.gasel.psm;

public class NumberPSM extends IntRangePSM {

	public NumberPSM(int from, int to) {
		super(from, to);
	}

	public String getLabel(int index) {
		if (index == 0) {
			return "--";
		}
		return getValue(index);
	}

	public Object getOption(int index) {
		if (index == 0) {
			return null;
		}
		return super.getOption(index - 1);
	}

	public int getOptionCount() {
		return super.getOptionCount() + 1;
	}

}
