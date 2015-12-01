package nc.ccas.gasel.pages.pi;

import nc.ccas.gasel.model.pi.AspectDossierPI;
import nc.ccas.gasel.pages.dossiers.RechercheAlias;

public abstract class Recherche extends RechercheAlias {

	public Recherche() {
		super(AspectDossierPI.class);
	}

}
