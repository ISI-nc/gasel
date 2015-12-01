package nc.ccas.gasel.model.pi;

import java.util.Date;

import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pi.auto._ControleEntretien;

public class ControleEntretien extends _ControleEntretien implements
		ModifListener {
	private static final long serialVersionUID = 115696549118458715L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getAttribution());
	}

}
