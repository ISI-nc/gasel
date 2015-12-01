package nc.ccas.gasel.services.fop;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IMarkupWriter;

public abstract class FoAbstractBlockContainer<T> extends FoAbstractElement<T> {

	private Object[] blockAttributes = new Object[] {};

	public FoAbstractBlockContainer(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoAbstractBlockContainer(IMarkupWriter writer) {
		super(writer);
	}

	public T blockAttributes(Object... blockAttributes) {
		Defense.notNull(blockAttributes, "blockAttributes");
		this.blockAttributes = blockAttributes;
		return fluentResult();
	}

	public FoBlock block(Object... attributes) {
		return child(FoBlock.class, //
				prependAttributes(attributes, blockAttributes));
	}

	public T textBlock(String text, Object... attributes) {
		block(attributes).print(text).end();
		return fluentResult();
	}

	public FoBlockContainer blockContainer(Object... attributes) {
		return child(FoBlockContainer.class, attributes);
	}

	public FoElement inline(Object... attributes) {
		return child("inline", attributes);
	}

}
