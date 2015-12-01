package nc.ccas.gasel.stats.repr;

import nc.ccas.gasel.stats.Representation;

public class BoolIntRepr extends Representation<Number> {

	private final String zeroString;
	private final String otherString;

	public BoolIntRepr(String zeroString, String otherString) {
		super(Number.class);
		this.zeroString = zeroString;
		this.otherString = otherString;
	}

	@Override
	public String representationImpl(Number obj) {
		return obj.intValue() == 0 ? zeroString : otherString;
	}

}
