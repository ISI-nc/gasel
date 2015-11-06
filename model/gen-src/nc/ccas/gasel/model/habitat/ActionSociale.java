package nc.ccas.gasel.model.habitat;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.habitat.auto._ActionSociale;

@Feminin
public class ActionSociale extends _ActionSociale implements ComplexDeletion,
		ModifListener {

	private static final long serialVersionUID = 3363050817821570488L;

	public void prepareForDeletion() {
		DeletionUtils.delete(getDetailsPrives());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
