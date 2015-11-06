package nc.ccas.gasel.model.paph;

import java.util.Date;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.paph.auto._AccompagnementPAPH;

public class AccompagnementPAPH extends _AccompagnementPAPH implements
		ComplexDeletion, ModifListener {

	private static final long serialVersionUID = -6719544820671816458L;

	@Override
	public String toString() {
		return getProjet();
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getObjectifs());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
