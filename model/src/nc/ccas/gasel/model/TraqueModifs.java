package nc.ccas.gasel.model;

import java.util.Date;

import nc.ccas.gasel.model.core.Utilisateur;

public interface TraqueModifs extends ModifListener {

	public Utilisateur getModifUtilisateur();

	public void setModifUtilisateur(Utilisateur utilisateur);

	public Date getModifDate();

	public void setModifDate(Date date);

}
