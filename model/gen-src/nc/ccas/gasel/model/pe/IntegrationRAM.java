package nc.ccas.gasel.model.pe;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pe.auto._IntegrationRAM;

@Feminin
public class IntegrationRAM extends _IntegrationRAM implements ModifListener {
	private static final long serialVersionUID = -7995225113123768731L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getAssistanteMaternelle());
	}

}
