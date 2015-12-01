package nc.ccas.gasel.model.pe;

import java.util.Date;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pe.auto._EnfantRAM;

public class EnfantRAM extends _EnfantRAM implements ComplexDeletion,
		ModifListener {
	private static final long serialVersionUID = 991835762659540335L;

	public void prepareForDeletion() {
		DeletionUtils.delete(getAbsences());
		DeletionUtils.delete(getAidesPaiement());
		DeletionUtils.delete(getAutorisations());
		DeletionUtils.delete(getGardes());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
