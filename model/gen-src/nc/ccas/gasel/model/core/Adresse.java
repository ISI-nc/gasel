package nc.ccas.gasel.model.core;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.core.auto._Adresse;

@Feminin
public class Adresse extends _Adresse {
	private static final long serialVersionUID = 4290009323038922319L;

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (getAutres() != null) {
			buf.append(getAutres());
		}

		if (getNumero() != null) {
			if (buf.length() > 0)
				buf.append(" -- ");
			buf.append(" " + getNumero());
		}
		if (getRue() != null) {
			if (buf.length() > 0)
				// buf.append(" -- ");
				buf.append(" " + getRue());
		}
		if (getCodePostal() != null && getVille() != null) {
			if (buf.length() > 0)
				buf.append(" -- ");
			buf.append("" + getCodePostal() + " " + getVille());
		}
		return buf.toString();
	}

}
