package nc.ccas.gasel.model.habitat.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.habitat.enums.auto._AssuranceHabitation;

@Feminin
public class AssuranceHabitation extends _AssuranceHabitation {

	private static final long serialVersionUID = -9132152798863193875L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



