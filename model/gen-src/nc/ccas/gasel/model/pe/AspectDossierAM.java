package nc.ccas.gasel.model.pe;

import java.util.Date;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pe.auto._AspectDossierAM;

public class AspectDossierAM extends _AspectDossierAM implements
		ComplexDeletion, ModifListener {
	private static final long serialVersionUID = 3966567694542978928L;

	public void prepareForDeletion() {
		DeletionUtils.delete(getAssistantesMaternelle());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
