package nc.ccas.gasel.model.pe;

import java.util.Date;

import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pe.auto._AutorisationEnfant;

public class AutorisationEnfant extends _AutorisationEnfant implements
		ModifListener {
	private static final long serialVersionUID = -8420772082495458538L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getEnfant());
	}

}
