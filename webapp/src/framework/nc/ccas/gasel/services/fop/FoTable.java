package nc.ccas.gasel.services.fop;

import org.apache.tapestry.IMarkupWriter;

public class FoTable extends FoAbstractElement<FoTable> {

	public FoTable(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoTable(IMarkupWriter writer) {
		super(writer);
	}

	@Override
	protected String tag() {
		return "table";
	}

	@Override
	protected FoTable fluentResult() {
		return this;
	}

	public FoTableHeader header() {
		return child(FoTableHeader.class);
	}

	public FoTableBody body() {
		return child(FoTableBody.class);
	}

	public FoTable column(Object... attributes) {
		return emptyChild("table-column", attributes);
	}

}
