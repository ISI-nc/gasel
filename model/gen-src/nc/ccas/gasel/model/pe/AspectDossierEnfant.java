package nc.ccas.gasel.model.pe;

import java.util.Date;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pe.auto._AspectDossierEnfant;

public class AspectDossierEnfant extends _AspectDossierEnfant implements
		ComplexDeletion, ModifListener {

	private static final long serialVersionUID = -5638315458837032857L;

	public EnfantRAM findEnfantRAM(Personne personne) {
		for (EnfantRAM enfant : getEnfantsRAM()) {
			if (enfant.getPersonne().equals(personne)) {
				return enfant;
			}
		}
		return null;
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getEnfantsRAM());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
