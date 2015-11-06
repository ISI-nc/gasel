package nc.ccas.gasel.utils;

public class CssStyleBuilder {

	private final StringBuilder buf = new StringBuilder();

	public CssStyleBuilder(String... clauses) {
		for (String clause : clauses) {
			append(clause);
		}
	}

	public CssStyleBuilder append(String clause) {
		if (clause == null)
			return this;
		if (buf.length() > 0)
			buf.append(";");
		buf.append(clause);
		return this;
	}

	@Override
	public String toString() {
		return buf.toString();
	}

}
