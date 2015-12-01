package nc.ccas.gasel.model;

import java.util.Date;

import nc.ccas.gasel.model.core.Utilisateur;

public interface ModifListener {

	/**
	 * Indique que l'objet a été modifié. Trigger invoqué juste avant
	 * l'enregistrement.
	 */
	public void modified(Utilisateur user, Date date);

}
