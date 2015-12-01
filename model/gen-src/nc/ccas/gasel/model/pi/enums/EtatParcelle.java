package nc.ccas.gasel.model.pi.enums;

import nc.ccas.gasel.model.pi.enums.auto._EtatParcelle;

public class EtatParcelle extends _EtatParcelle {

	private static final long serialVersionUID = 1810503961720592694L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



