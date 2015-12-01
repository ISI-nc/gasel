package nc.ccas.gasel.services.fop;

import org.apache.tapestry.IMarkupWriter;

public abstract class FoAbstractElement<R> extends FoContributor {

	/**
	 * @return The tag of this element ("page-sequence", "table-row", etc).
	 */
	protected abstract String tag();

	/**
	 * @return this (Java limitation: cannot specify "my class")
	 */
	protected abstract R fluentResult();

	public FoAbstractElement(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoAbstractElement(IMarkupWriter writer) {
		super(writer);
	}

	public R begin(Object... attributes) {
		super.begin(tag(), attributes);
		return fluentResult();
	}

	public void end() {
		super.end(tag());
	}

	public FoElement child(String tag, Object... attributes) {
		return new FoElement(tag, writer, xmlns).begin(attributes);
	}

	public <T extends FoAbstractElement<T>> T child(Class<T> clazz,
			Object... attributes) {
		T child;
		try {
			child = clazz.getConstructor(IMarkupWriter.class, String.class)
					.newInstance(writer, xmlns);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		child.begin(attributes);

		return child;
	}

	public R emptyChild(String tag, Object... attributes) {
		empty(tag, attributes);
		return fluentResult();
	}

	// ------

	public R print(char value) {
		writer.print(value);
		return fluentResult();
	}

	public R print(char[] data, int offset, int length, boolean raw) {
		writer.print(data, offset, length, raw);
		return fluentResult();
	}

	public R print(char[] data, int offset, int length) {
		writer.print(data, offset, length);
		return fluentResult();
	}

	public R print(int value) {
		writer.print(value);
		return fluentResult();
	}

	public R print(String value, boolean raw) {
		writer.print(value, raw);
		return fluentResult();
	}

	public R print(String value) {
		writer.print(value);
		return fluentResult();
	}

	public R println() {
		writer.println();
		return fluentResult();
	}

}
