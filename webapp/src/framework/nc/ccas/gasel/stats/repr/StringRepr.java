package nc.ccas.gasel.stats.repr;

import nc.ccas.gasel.stats.Representation;

public class StringRepr extends Representation<Object> {

	public static final StringRepr INSTANCE = new StringRepr();

	protected StringRepr() {
		super(Object.class);
	}

	@Override
	public String representationImpl(Object obj) {
		return obj.toString();
	}

}
