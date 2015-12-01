package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._Problematique;

@Feminin
public class Problematique extends _Problematique implements Enumeration {

	private static final long serialVersionUID = 3483645068072706321L;

	@Override
	public String toString() {
		return getLibelle();
	}

}
