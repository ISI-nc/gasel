package nc.ccas.gasel.model.vues;

import java.util.Date;
import java.util.List;

import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.vues.auto._AideResumeMontants;
import nc.ccas.gasel.modelUtils.DateUtils;

public class AideResumeMontants extends _AideResumeMontants {
	private static final long serialVersionUID = -7876555622243049219L;

	public Date getDebut() {
		return DateUtils.mois(getAnnee(), getMois())[0];
	}

	public Date getFin() {
		return DateUtils.mois(getAnnee(), getMois())[1];
	}

	public List<Bon> getBons() {
		return getAide().bonsMois(getDebut());
	}

	public int getAnneeMois() {
		return (Integer) getObjectId().getIdSnapshot().get("annee_mois");
	}

	public Integer getMontantUtilise() {
		return isFutur() ? 0 : getMontantBonsUtilise() + getMontantBonsEdite();
	}

	public Integer getMontantInutilise() {
		return isFutur() ? getMontantAide() : getMontantBonsInutilise();
	}

	public Integer getMontantEngage() {
		return isFutur() ? getMontantAide() : getMontantBons();
	}

	public boolean isFutur() {
		return DateUtils.anneeMois(new Date()) < getAnneeMois();
	}

}
