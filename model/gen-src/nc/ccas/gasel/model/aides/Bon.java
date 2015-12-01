package nc.ccas.gasel.model.aides;

import java.util.Date;
import java.util.List;

import org.apache.cayenne.DataObjectUtils;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.TraqueModifs;
import nc.ccas.gasel.model.aides.auto._Bon;
import nc.ccas.gasel.model.core.Utilisateur;

public class Bon extends _Bon implements ComplexDeletion, TraqueModifs {
	private static final long serialVersionUID = -3815380263667739150L;

	public static int extractSeqNum(String ref) {
		return Integer.parseInt(ref.substring(6, 10));
	}

	public void setEtat(int etat) {
		setEtat(etat, true);
	}

	public void setEtat(int etat, boolean checkTransition) {
		setEtat((EtatBon) DataObjectUtils.objectForPK(getObjectContext(), EtatBon.class, etat),
				checkTransition);
	}

	@Override
	public void setEtat(EtatBon etat) {
		setEtat(etat, true);
	}

	public void setEtat(EtatBon etat, boolean checkTransition) {
		if (etat == null)
			throw new NullPointerException("état null");

		if (getEtat() == null) {
			if (etat.isCree()) {
				super.setEtat(etat);
			} else {
				setEtat(EtatBon.CREE);
			}
		}

		if (checkTransition) {
			getEtat().checkTransition(etat);
		}

		super.setEtat(etat);
	}

	public UsageBon getUsage() {
		List<UsageBon> usages = getUsages();
		if (usages.isEmpty())
			return null;
		return usages.get(0);
	}

	public void setUsage(UsageBon usage) {
		UsageBon usageOrig = getUsage();
		if (usageOrig == usage)
			return;
		if (usageOrig != null && usageOrig.equals(usage))
			return;

		if (usageOrig != null)
			removeFromUsages(usageOrig);

		if (usage == null)
			return;

		addToUsages(usage);
		if (usage.getBon() != this) {
			usage.setBon(this);
		}
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getUsage());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.updateTraqueModifs(this, user, date);
		ModifUtils.triggerModified(user, date, getAide());
	}

	@Override
	public String toString() {
		return getNumero();
	}

}
