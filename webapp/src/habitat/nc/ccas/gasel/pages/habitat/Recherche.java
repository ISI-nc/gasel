package nc.ccas.gasel.pages.habitat;

import nc.ccas.gasel.model.habitat.AspectDossierHabitat;
import nc.ccas.gasel.pages.dossiers.RechercheAlias;

public abstract class Recherche extends RechercheAlias {

	public Recherche() {
		super(AspectDossierHabitat.class);
	}

}
