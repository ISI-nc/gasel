package nc.ccas.gasel.model.habitat.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.habitat.enums.auto._AssuranceLocative;

@Feminin
public class AssuranceLocative extends _AssuranceLocative {

	private static final long serialVersionUID = 4102773267600851157L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



