package nc.ccas.gasel.model.habitat;

import java.util.Date;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.habitat.auto._ObjectifProbHabitat;

public class ObjectifProbHabitat extends _ObjectifProbHabitat implements
		ComplexDeletion, ModifListener {

	private static final long serialVersionUID = 7216798308590803933L;

	public void prepareForDeletion() {
		DeletionUtils.empty(this, REPONSES_PROPERTY);
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getObjectif());
	}

}
