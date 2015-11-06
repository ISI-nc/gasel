package nc.ccas.gasel.pages.pi;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pi.Parcelle;

public abstract class EditerParcelle extends EditPage<Parcelle> {

	public EditerParcelle() {
		super(Parcelle.class);
	}
}