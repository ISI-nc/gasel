package nc.ccas.gasel.services.fop;

import org.apache.tapestry.IMarkupWriter;

public class FoBlockContainer extends FoAbstractBlockContainer<FoBlockContainer> {

	public FoBlockContainer(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoBlockContainer(IMarkupWriter writer) {
		super(writer);
	}

	@Override
	protected FoBlockContainer fluentResult() {
		return this;
	}

	@Override
	protected String tag() {
		return "block-container";
	}

}
