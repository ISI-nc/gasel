package nc.ccas.gasel.model.core;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.auto._ActivitePersonne;

@Feminin
public class ActivitePersonne extends _ActivitePersonne implements ModifListener {

	private static final long serialVersionUID = -5072815528476148454L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getPersonne());
	}

}



