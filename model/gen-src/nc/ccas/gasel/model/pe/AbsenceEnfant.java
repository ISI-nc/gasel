package nc.ccas.gasel.model.pe;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pe.auto._AbsenceEnfant;

@Feminin
public class AbsenceEnfant extends _AbsenceEnfant implements ModifListener {

	private static final long serialVersionUID = 6613905263864973520L;

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getEnfant());
	}

}



