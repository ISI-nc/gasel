package nc.ccas.gasel.jwcs.core.tableau;

import java.text.Format;

public class Cellule {

	private final Object value;

	private final Format format;

	private final boolean highlight;

	private final boolean dottedTop;

	public Cellule(Object value, Format format, boolean highlight,
			boolean dottedTop) {
		this.value = value;
		this.format = format;
		this.highlight = highlight;
		this.dottedTop = dottedTop;
	}

	public Format getFormat() {
		return format;
	}

	public Object getValue() {
		return value;
	}

	public boolean isHighlight() {
		return highlight;
	}

	public boolean isDottedTop() {
		return dottedTop;
	}

}
