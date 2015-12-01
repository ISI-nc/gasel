package nc.ccas.gasel.jwcs.dossiers;

import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.model.core.Charge;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.core.Ressource;

import org.apache.cayenne.DataObject;

public abstract class ResCharge extends BaseComponent {

	public abstract DataObject getRes();

	public abstract Personne getPersonne();

	public abstract void setPersonne(Personne personne);

	public abstract String getListe();

	public String getType() {
		if ("ressources".equals(getListe())) {
			return "Ressource";
		} else if ("charges".equals(getListe())) {
			return "Charge";
		} else {
			throw new IllegalArgumentException(getListe());
		}
	}

	public String getEnumToShow() {
		if (getRes() instanceof Ressource) {
			return "TypeRessource";
		} else if (getRes() instanceof Charge) {
			return "TypeCharge";
		}
		throw new IllegalArgumentException(String.valueOf(getRes()));
	}

}
