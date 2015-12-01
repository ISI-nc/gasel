package nc.ccas.gasel.model.paph;

import java.util.Date;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.paph.auto._ObjectifProbPAPH;

public class ObjectifProbPAPH extends _ObjectifProbPAPH implements
		ComplexDeletion, ModifListener {

	private static final long serialVersionUID = -8149479283902974101L;

	public void prepareForDeletion() {
		DeletionUtils.empty(this, REPONSES_PROPERTY);
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getObjectif());
	}

}
