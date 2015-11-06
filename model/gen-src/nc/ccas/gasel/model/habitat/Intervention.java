package nc.ccas.gasel.model.habitat;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.habitat.auto._Intervention;

@Feminin
public class Intervention extends _Intervention implements ModifListener {

	private static final long serialVersionUID = 2530924909056025579L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getObjectif());
	}

}
