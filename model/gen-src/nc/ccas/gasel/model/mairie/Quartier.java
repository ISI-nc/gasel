package nc.ccas.gasel.model.mairie;

import nc.ccas.gasel.model.mairie.auto._Quartier;

public class Quartier extends _Quartier {
	private static final long serialVersionUID = 2667522565453915191L;

	public String getLibelle() {
		return getLiquar();
	}

	@Override
	public String toString() {
		return getLibelle();
	}

}
