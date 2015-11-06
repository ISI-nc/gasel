package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._TypeContrat;

public class TypeContrat extends _TypeContrat implements Enumeration {

	private static final long serialVersionUID = -5869856106707440129L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



