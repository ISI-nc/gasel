package nc.ccas.gasel.services.fop;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IMarkupWriter;

public class FoTableRow extends FoAbstractElement<FoTableRow> {

	private Object[] cellAttributes = new Object[] {};

	public FoTableRow(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoTableRow(IMarkupWriter writer) {
		super(writer);
	}

	@Override
	protected String tag() {
		return "table-row";
	}

	public FoTableRow cellAttributes(Object... cellAttributes) {
		Defense.notNull(cellAttributes, "cellAttributes");
		this.cellAttributes = cellAttributes;
		return this;
	}

	public FoTableCell cell(Object... attributes) {
		attributes = prependAttributes(attributes, cellAttributes);
		return new FoTableCell(writer, xmlns).begin(attributes);
	}

	public FoTableRow textCell(String text, Object... attributes) {
		cell(attributes).text(text).end();
		return this;
	}

	@Override
	protected FoTableRow fluentResult() {
		return this;
	}

}
