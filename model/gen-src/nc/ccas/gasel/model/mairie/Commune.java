package nc.ccas.gasel.model.mairie;

import nc.ccas.gasel.model.mairie.auto._Commune;

public class Commune extends _Commune implements Ville {
	private static final long serialVersionUID = -3726821026633553572L;

	public CodePostal getCodePostal() {
		CodePostal retval = null;
		for (CodePostal cp : getCodesPostaux()) {
			if (retval == null || cp.getCode() < retval.getCode()) {
				retval = cp;
			}
		}
		return retval;
	}

	@Override
	public String toString() {
		return getLibelle();
	}

}
