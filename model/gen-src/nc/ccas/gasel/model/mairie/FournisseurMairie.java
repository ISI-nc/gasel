package nc.ccas.gasel.model.mairie;

import nc.ccas.gasel.model.mairie.auto._FournisseurMairie;

public class FournisseurMairie extends _FournisseurMairie {
	private static final long serialVersionUID = -2411292244715198101L;

	public boolean isActif() {
		return getEstActif() != null && "O".equalsIgnoreCase(getEstActif());
	}

	@Override
	public String toString() {
		return String.valueOf(getLibelle()).trim();
	}

}
