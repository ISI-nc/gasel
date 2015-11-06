package nc.ccas.gasel.model.paph;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.paph.auto._DemandeTaxi;

@Feminin
public class DemandeTaxi extends _DemandeTaxi implements ComplexDeletion,
		ModifListener {

	private static final long serialVersionUID = 7063235193513170055L;

	public void prepareForDeletion() {
		DeletionUtils.delete(getSeriesTickets());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
