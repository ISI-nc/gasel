package nc.ccas.gasel.modelUtils;

public class DateDelta {

	public int annees;

	public int mois;

	public int jours;

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		if (annees != 0) {
			buf.append(String.valueOf(annees)).append(
					" année" + (annees > 1 || annees < 1 ? "s" : ""));
		}
		if (mois != 0) {
			if (buf.length() > 0)
				buf.append(", ");
			buf.append(String.valueOf(mois)).append(" mois");
		}
		if (jours != 0) {
			if (buf.length() > 0)
				buf.append(", ");
			buf.append(String.valueOf(jours)).append(
					" jour" + (jours > 1 || jours < 1 ? "s" : ""));
		}
		return buf.toString();
	}

}
