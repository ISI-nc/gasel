package nc.ccas.gasel.pages.paph;

import java.util.Date;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.paph.AspectDossierPAPH;
import nc.ccas.gasel.model.paph.DossierPAPH;

public abstract class ListeDossiersPaPh extends EditPage<AspectDossierPAPH> {

	public ListeDossiersPaPh() {
		super(AspectDossierPAPH.class);
	}

	@Override
	protected void prepareEnregistrer() {
		Dossier dossier = getDossier();
		dossier.setModifDate(new Date());
	}

	public Dossier getDossier() {
		return getObject().getDossier();
	}

	public void editerDossierPaPh(Personne personne) {
		EditerDossierPaPh page = (EditerDossierPaPh) getRequestCycle().getPage(
				"paph/EditerDossierPaPh");
		// Recherche du dossier
		DossierPAPH dossierPersonne = findDossier(personne);
		if (dossierPersonne == null) {
			// Pas trouvé
			page.activateForParent(getObject(), "dossier", //
					"personne", personne); // et telle personne
		} else {
			page.openWithParent(dossierPersonne, "dossier");
		}
	}

	public DossierPAPH findDossier(Personne personne) {
		for (DossierPAPH dossier : getObject().getDossiers()) {
			if (dossier.getPersonne().equals(personne)) {
				return dossier;
			}
		}
		return null;
	}
}
