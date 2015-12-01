package nc.ccas.gasel.services.fop;

import org.apache.tapestry.IMarkupWriter;

public class FoTableBody extends FoTableRowContainer<FoTableBody> {

	public FoTableBody(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoTableBody(IMarkupWriter writer) {
		super(writer);
	}

	@Override
	protected String tag() {
		return "table-body";
	}

	@Override
	protected FoTableBody fluentResult() {
		return this;
	}

}
