package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._StatutPersonne;

public class StatutPersonne extends _StatutPersonne implements Enumeration {
	private static final long serialVersionUID = -5539827801892725938L;

	@Override
	public String toString() {
		return getLibelle();
	}

}
