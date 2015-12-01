package nc.ccas.gasel.services.fop;

import org.apache.tapestry.IMarkupWriter;

public class FoContributor {

	protected final IMarkupWriter writer;
	protected final String xmlns;

	public FoContributor(IMarkupWriter writer) {
		this(writer, "fo");
	}

	public FoContributor(IMarkupWriter writer, String xmlns) {
		this.writer = writer;
		this.xmlns = xmlns;
	}

	protected void begin(String name, Object... attributes) {
		writer.begin(xmlns + ":" + name);
		attributes(attributes);
	}

	protected void end(String tag) {
		writer.end(xmlns + ":" + tag);
	}

	protected void empty(String name, Object... attributes) {
		writer.beginEmpty(xmlns + ":" + name);
		attributes(attributes);
	}

	protected void attributes(Object... attributes) {
		if (attributes.length % 2 != 0)
			throw new IllegalArgumentException("Wrong number of attributes");
		for (int i = 0; i < attributes.length; i += 2) {
			String attribute = (String) attributes[i];
			Object value = attributes[i + 1];

			if (value == null)
				continue;

			if (value instanceof Boolean) {
				writer.attribute(attribute, (Boolean) value);
			} else if (value instanceof Integer) {
				writer.attribute(attribute, (Integer) value);
			} else if (value instanceof String) {
				writer.attribute(attribute, (String) value);
			} else {
				writer.attribute(attribute, value.toString());
			}
		}
	}

	protected Object[] prependAttributes(Object[] attributes,
			Object... extraAttributes) {
		Object[] attrs = new Object[attributes.length + extraAttributes.length];

		int i = 0;
		for (Object o : extraAttributes)
			attrs[i++] = o;

		for (Object o : attributes)
			attrs[i++] = o;

		return attrs;
	}

}
