package nc.ccas.gasel.model.core;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.auto._Ressource;

@Feminin
public class Ressource extends _Ressource implements RessCharge, ModifListener {
	private static final long serialVersionUID = -6922448087022021563L;

	public String getLibelle() {
		if (getType() == null)
			return "--";
		return getType().getLibelle();
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getPersonne());
	}

}
