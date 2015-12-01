package nc.ccas.gasel.model.paph;

import java.util.Date;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.paph.auto._DossierPAPH;

public class DossierPAPH extends _DossierPAPH implements ComplexDeletion,
		ModifListener {

	private static final long serialVersionUID = -6994088222438921352L;

	public DossierPAPH() {
		setEstHandicape(false);
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getAccompagnements());
		DeletionUtils.delete(getDemandesTaxi());
		DeletionUtils.delete(getDeplacementsTaxi());
		DeletionUtils.delete(getHandicaps());
		DeletionUtils.delete(getProcurations());
		DeletionUtils.delete(getSpecificitesCarte());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
