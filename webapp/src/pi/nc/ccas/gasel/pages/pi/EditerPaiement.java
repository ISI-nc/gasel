package nc.ccas.gasel.pages.pi;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pi.Paiement;

public abstract class EditerPaiement extends EditPage<Paiement> {

	public EditerPaiement() {
		super(Paiement.class);
	}

	public Paiement getPaiement() {
		return getObject();
	}

}
