package nc.ccas.gasel.model.aides;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.aides.auto._AideRefusee;
import nc.ccas.gasel.model.core.Utilisateur;

@Feminin
public class AideRefusee extends _AideRefusee implements ModifListener {
	private static final long serialVersionUID = -6955391223383843478L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}



