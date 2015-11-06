package nc.ccas.gasel.services.fop;

import org.apache.tapestry.IMarkupWriter;

public class FoFlow extends FoAbstractBlockContainer<FoFlow> {

	public FoFlow(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoFlow(IMarkupWriter writer) {
		super(writer);
	}

	@Override
	protected String tag() {
		return "flow";
	}

	@Override
	protected FoFlow fluentResult() {
		return this;
	}

}
