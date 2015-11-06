package nc.ccas.gasel.model.paph;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.paph.auto._ProcurationPAPH;

@Feminin
public class ProcurationPAPH extends _ProcurationPAPH implements ModifListener {
	private static final long serialVersionUID = -7024947781017733256L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
