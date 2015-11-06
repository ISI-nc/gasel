package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._TypePublic;

public class TypePublic extends _TypePublic implements Enumeration {

	private static final long serialVersionUID = 9090937949690537606L;

	@Override
	public String toString() {
		return getLibelle();
	}

}
