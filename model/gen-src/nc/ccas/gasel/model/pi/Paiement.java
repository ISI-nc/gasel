package nc.ccas.gasel.model.pi;

import java.util.Date;

import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pi.auto._Paiement;

public class Paiement extends _Paiement implements ModifListener {
	private static final long serialVersionUID = -4038662010806683173L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getAttribution());
	}

}
