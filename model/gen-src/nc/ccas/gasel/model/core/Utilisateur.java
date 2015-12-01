package nc.ccas.gasel.model.core;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.core.auto._Utilisateur;

public class Utilisateur extends _Utilisateur implements ComplexDeletion {
	private static final long serialVersionUID = -1828114337282411541L;
	
	public Utilisateur() {
		setActif(true);
	}

	@Override
	public String toString() {
		return getInitiales() + " (" + getPrenom() + " " + getNom() + ")";
	}

	public String getDefaultInitiales() {
		return (getPrenom().substring(0, 1) + getNom().substring(0, 1))
				.toUpperCase();
	}

	public void prepareForDeletion() {
		DeletionUtils.empty(this, GROUPES_PROPERTY);
	}
	
	public boolean getHasAccesDirection() {
		for (Object o : getGroupes()) {
			Groupe g = (Groupe) o;
			if (g.getLibelle().toLowerCase().contains("direction")) {
				return true;
			}
		}
		return false;
	}

}
