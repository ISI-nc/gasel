package nc.ccas.gasel.model.paph.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.paph.enums.auto._SpecificiteCarteHand;

@Feminin
public class SpecificiteCarteHand extends _SpecificiteCarteHand {

	private static final long serialVersionUID = -2426116118764440253L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



