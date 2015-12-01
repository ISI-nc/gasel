package nc.ccas.gasel.model.paph;

import java.util.Date;

import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.paph.auto._DeplacementTaxi;

public class DeplacementTaxi extends _DeplacementTaxi implements ModifListener {

	private static final long serialVersionUID = 7531909405951929589L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
