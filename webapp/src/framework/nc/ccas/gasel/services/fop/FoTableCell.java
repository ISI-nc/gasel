package nc.ccas.gasel.services.fop;

import org.apache.tapestry.IMarkupWriter;

public class FoTableCell extends FoAbstractElement<FoTableCell> {

	public FoTableCell(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoTableCell(IMarkupWriter writer) {
		super(writer);
	}

	@Override
	protected String tag() {
		return "table-cell";
	}

	public FoTableCell text(String text, Object... attributes) {
		child("block", attributes).print(text).end();
		return this;
	}

	@Override
	protected FoTableCell fluentResult() {
		return this;
	}

}
