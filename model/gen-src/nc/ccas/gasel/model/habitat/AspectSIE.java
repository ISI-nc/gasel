package nc.ccas.gasel.model.habitat;

import java.util.Date;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.habitat.auto._AspectSIE;

public class AspectSIE extends _AspectSIE implements ComplexDeletion , ModifListener {

	private static final long serialVersionUID = 7670723159090714782L;

	public void prepareForDeletion() {
		DeletionUtils.delete(getSecours());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
