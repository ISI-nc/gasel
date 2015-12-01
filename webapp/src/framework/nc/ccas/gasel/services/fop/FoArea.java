package nc.ccas.gasel.services.fop;

import org.apache.tapestry.IMarkupWriter;

public class FoArea extends FoAbstractBlockContainer<FoArea> {

	public FoArea(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoArea(IMarkupWriter writer) {
		super(writer);
	}

	@Override
	protected String tag() {
		return "area";
	}

	@Override
	protected FoArea fluentResult() {
		return this;
	}

}
