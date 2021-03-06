package nc.ccas.gasel.model.paph;

import java.util.Date;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.paph.auto._AspectDossierPAPH;

public class AspectDossierPAPH extends _AspectDossierPAPH implements
		ComplexDeletion, ModifListener {

	private static final long serialVersionUID = 8508493733732094356L;

	public void prepareForDeletion() {
		DeletionUtils.delete(getDossiers());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
