package nc.ccas.gasel.model.paph.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.paph.enums.auto._NatureDeplacementTaxi;

@Feminin
public class NatureDeplacementTaxi extends _NatureDeplacementTaxi {

	private static final long serialVersionUID = -3654818367105422704L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



