package nc.ccas.gasel.services.fop;

import org.apache.tapestry.IMarkupWriter;

public class FoBlock extends FoAbstractBlockContainer<FoBlock> {

	public FoBlock(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoBlock(IMarkupWriter writer) {
		super(writer);
	}

	@Override
	protected FoBlock fluentResult() {
		return this;
	}

	@Override
	protected String tag() {
		return "block";
	}

}
