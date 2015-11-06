package nc.ccas.gasel.services.fop;

import org.apache.tapestry.IMarkupWriter;

public class FoTableHeader extends FoTableRowContainer<FoTableHeader> {

	public FoTableHeader(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoTableHeader(IMarkupWriter writer) {
		super(writer);
	}

	@Override
	protected String tag() {
		return "table-header";
	}

	@Override
	protected FoTableHeader fluentResult() {
		return this;
	}

}
