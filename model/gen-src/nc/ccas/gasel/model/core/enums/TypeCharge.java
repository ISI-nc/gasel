package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._TypeCharge;

public class TypeCharge extends _TypeCharge implements Enumeration {
	private static final long serialVersionUID = -8294030761965691443L;

	@Override
	public String toString() {
		return getLibelle();
	}

}



