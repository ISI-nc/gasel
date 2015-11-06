package nc.ccas.gasel.model.paph;

import java.util.Date;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.paph.auto._ObjectifPAPH;

public class ObjectifPAPH extends _ObjectifPAPH implements ComplexDeletion,
		ModifListener {

	private static final long serialVersionUID = 6385831590027625117L;

	@Override
	public String toString() {
		return getLibelle();
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getProblematiques());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getAccompagnement());
	}

}
