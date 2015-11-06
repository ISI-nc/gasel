package nc.ccas.gasel.model.habitat;

import java.util.Date;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.habitat.auto._ObjectifHabitat;

public class ObjectifHabitat extends _ObjectifHabitat implements
		ComplexDeletion, ModifListener {

	private static final long serialVersionUID = 8215596614245531888L;

	public void prepareForDeletion() {
		DeletionUtils.delete(getInterventions());
		DeletionUtils.delete(getProblematiques());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getActionSociale());
	}

}
