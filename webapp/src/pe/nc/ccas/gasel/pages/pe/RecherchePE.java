package nc.ccas.gasel.pages.pe;

import nc.ccas.gasel.model.pe.AspectDossierEnfant;
import nc.ccas.gasel.pages.dossiers.RechercheAlias;

public abstract class RecherchePE extends RechercheAlias {

	public RecherchePE() {
		super(AspectDossierEnfant.class);
	}

}
