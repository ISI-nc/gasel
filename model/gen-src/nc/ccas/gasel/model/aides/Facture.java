package nc.ccas.gasel.model.aides;

import static java.util.Collections.singletonList;
import static nc.ccas.gasel.modelUtils.CommonQueries.prefetch;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.TraqueModifs;
import nc.ccas.gasel.model.aides.auto._Facture;
import nc.ccas.gasel.model.core.Utilisateur;

@Feminin
public class Facture extends _Facture implements ComplexDeletion, TraqueModifs {
	private static final long serialVersionUID = -5843110130365267441L;

	public int getTotal() {
		int total = 0;
		for (PartieFacture partie : getParties()) {
			total += partie.getTotal();
		}
		return total;
	}

	public int getTotalAlloue() {
		int total = 0;
		for (PartieFacture partie : getParties()) {
			total += partie.getTotalAlloue();
		}
		return total;
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getParties());
	}

	public Integer getMontantBons() {
		prefetch(getObjectContext(), singletonList(this), "parties.bons.bon");

		int sum = 0;
		for (PartieFacture partie : getParties()) {
			for (UsageBon bon : partie.getBons()) {
				sum += bon.getMontantUtilise();
			}
		}
		return sum;
	}
	
	public void modified(Utilisateur user, Date date) {
		ModifUtils.updateTraqueModifs(this, user, date);
		// TODO : trigger sur les bons new/modified
	}

}
