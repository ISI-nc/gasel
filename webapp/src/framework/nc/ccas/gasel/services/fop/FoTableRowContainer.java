package nc.ccas.gasel.services.fop;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IMarkupWriter;

public abstract class FoTableRowContainer<R> extends FoAbstractElement<R> {

	private Object[] rowAttributes = new Object[] {};

	private Object[] cellAttributes = new Object[] {};

	public FoTableRowContainer(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoTableRowContainer(IMarkupWriter writer) {
		super(writer);
	}

	public R rowAttributes(Object... rowAttributes) {
		Defense.notNull(rowAttributes, "rowAttributes");
		this.rowAttributes = rowAttributes;
		return fluentResult();
	}

	public R cellAttributes(Object... cellAttributes) {
		Defense.notNull(cellAttributes, "cellAttributes");
		this.cellAttributes = cellAttributes;
		return fluentResult();
	}

	public FoTableRow row(Object... attributes) {
		if (attributes.length == 0)
			attributes = rowAttributes;

		return child(FoTableRow.class, attributes) //
				.cellAttributes(cellAttributes);
	}

}
