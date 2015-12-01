package nc.ccas.gasel.pages.compta;

import java.util.List;

import nc.ccas.gasel.ObjectCallbackPage;
import nc.ccas.gasel.model.aides.Bon;

public abstract class Consultation extends ObjectCallbackPage<Bon> {

	public abstract List<Bon> getResults();

	public Consultation() {
	}

	public int getMontantTotal() {
		int total = 0;
		for (Bon bon : getResults()) {
			if (bon.getMontant() == null) {
				continue;
			}
			total += bon.getUsage().getMontantUtilise();
		}
		return total;
	}
}
