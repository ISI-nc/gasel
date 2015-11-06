package nc.ccas.gasel.services.fop;

import org.apache.tapestry.IMarkupWriter;

public class FoStaticContent extends FoAbstractBlockContainer<FoStaticContent> {

	public FoStaticContent(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoStaticContent(IMarkupWriter writer) {
		super(writer);
	}

	@Override
	protected String tag() {
		return "static-content";
	}

	@Override
	protected FoStaticContent fluentResult() {
		return this;
	}

}
