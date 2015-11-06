package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._TypeActivite;

public class TypeActivite extends _TypeActivite implements Enumeration {
	private static final long serialVersionUID = -2704770372860471162L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



