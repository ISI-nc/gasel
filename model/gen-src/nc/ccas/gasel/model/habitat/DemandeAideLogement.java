package nc.ccas.gasel.model.habitat;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.habitat.auto._DemandeAideLogement;

@Feminin
public class DemandeAideLogement extends _DemandeAideLogement implements
		ComplexDeletion, ModifListener {

	private static final long serialVersionUID = 2089364479772473389L;

	public void prepareForDeletion() {
		DeletionUtils.delete(getAides());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossierHabitat());
	}

}
