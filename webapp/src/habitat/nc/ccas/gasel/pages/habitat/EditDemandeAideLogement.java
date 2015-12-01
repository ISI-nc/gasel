package nc.ccas.gasel.pages.habitat;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.habitat.AideLogement;
import nc.ccas.gasel.model.habitat.DemandeAideLogement;

public abstract class EditDemandeAideLogement extends
		EditPage<DemandeAideLogement> {

	public EditDemandeAideLogement() {
		super(DemandeAideLogement.class);
	}

	@Override
	protected void prepareNewObject(DemandeAideLogement object) {
		//getObject().setAttribution(createObject(Attribution.class));
	}

	public abstract AideLogement getAide();

	public void initAide() {
		AideLogement aide = getAide();
		aide.setAccordCommission(false);
	}

	/*
	 * public String getDuree() { Date commission = getObject().getDateCommAL();
	 * Date renouvellement = getObject().getRenouvellement(); if (commission ==
	 * null || renouvellement == null) { return null; } return
	 * dates.sub(renouvellement, commission).toString(); }
	 */
}
