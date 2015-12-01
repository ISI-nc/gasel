package nc.ccas.gasel.model.pi;

import static nc.ccas.gasel.modelUtils.DateUtils.addMonths;
import static nc.ccas.gasel.modelUtils.DateUtils.today;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pi.auto._DemandeJF;

import org.apache.cayenne.DataObject;

@Feminin
public class DemandeJF extends _DemandeJF implements ComplexDeletion,
		ModifListener {

	private static final long serialVersionUID = 8645738468577208685L;

	public AttributionJF getAttribution() {
		return getAttributions().isEmpty() ? null : getAttributions().get(0);
	}

	public boolean isActive() {
		Date date = getDate();

		if (date == null)
			return false;

		Date dateLimite = addMonths(today(), -6);
		return date.after(dateLimite);
	}

	/**
	 * @return collectivite ou dossier, en fonction de celui qui est défini.
	 */
	public DataObject getDemandeur() {
		if (getCollectivite() != null) {
			return getCollectivite();
		}
		return getDossier().getDossier();
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getAttribution());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDemandeur());
	}

}
