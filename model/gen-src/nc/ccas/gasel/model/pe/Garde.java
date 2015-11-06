package nc.ccas.gasel.model.pe;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pe.auto._Garde;

@Feminin
public class Garde extends _Garde implements ModifListener {
	private static final long serialVersionUID = -3043862220478236164L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, //
				getEnfant(), //
				getAssistanteMaternelle());
	}

}
