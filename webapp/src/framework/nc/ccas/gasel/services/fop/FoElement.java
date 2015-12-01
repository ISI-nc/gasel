package nc.ccas.gasel.services.fop;

import org.apache.tapestry.IMarkupWriter;

public class FoElement extends FoAbstractElement<FoElement> {

	private final String tag;

	public FoElement(String tag, IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
		this.tag = tag;
	}

	public FoElement(String tag, IMarkupWriter writer) {
		super(writer);
		this.tag = tag;
	}

	@Override
	protected FoElement fluentResult() {
		return this;
	}

	@Override
	protected String tag() {
		return tag;
	}

}
