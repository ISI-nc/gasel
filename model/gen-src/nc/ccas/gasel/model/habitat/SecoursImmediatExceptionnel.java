package nc.ccas.gasel.model.habitat;

import java.util.Date;

import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.habitat.auto._SecoursImmediatExceptionnel;

public class SecoursImmediatExceptionnel extends _SecoursImmediatExceptionnel
		implements ModifListener {
	private static final long serialVersionUID = -8446830450156300800L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
