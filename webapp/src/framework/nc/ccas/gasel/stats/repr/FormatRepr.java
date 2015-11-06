package nc.ccas.gasel.stats.repr;

import java.text.Format;

import nc.ccas.gasel.stats.Representation;

public class FormatRepr extends Representation<Object> {

	private final Format format;

	public FormatRepr(Format format) {
		super(Object.class);
		this.format = format;
	}

	@Override
	public String representationImpl(Object obj) {
		return format.format(obj);
	}

}
