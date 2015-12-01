package nc.ccas.gasel.model.paph;

import java.util.Date;

import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.paph.auto._HandicapPAPH;

public class HandicapPAPH extends _HandicapPAPH implements ModifListener {
	private static final long serialVersionUID = -3591941117450216385L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
