package nc.ccas.gasel.model.pe;

import java.util.Date;

import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pe.auto._HandicapPE;

public class HandicapPE extends _HandicapPE implements ModifListener {

	private static final long serialVersionUID = 1575376867918638065L;

	@Override
	public String toString() {
		return getLibelle();
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getEnfant());
	}

}
