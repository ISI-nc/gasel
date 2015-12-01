package nc.ccas.gasel.model.core;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.auto._Charge;

@Feminin
public class Charge extends _Charge implements RessCharge, ModifListener {
	private static final long serialVersionUID = -5511463572272947305L;

	public String getLibelle() {
		return getType().getLibelle();
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getPersonne());
	}

}
