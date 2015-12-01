package nc.ccas.gasel.model.paph;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.paph.auto._SpecificiteCartePAPH;

@Feminin
public class SpecificiteCartePAPH extends _SpecificiteCartePAPH implements
		ModifListener {
	private static final long serialVersionUID = 6630317682608423818L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
