package nc.ccas.gasel.model.core;

import java.util.Date;

public interface RessCharge {

	public Integer getMontant();

	public void setMontant(Integer montant);

	public String getLibelle();

	public Date getDebut();

	public void setDebut(Date date);

	public Date getFin();

	public void setFin(Date date);
	
	public Personne getPersonne();
	
	public void setPersonne(Personne personne);

}
