package nc.ccas.gasel.pages.parametres.pi;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pi.enums.EtatParcelle;

public abstract class EditerEtatParcelle extends EditPage<EtatParcelle> {

	public EditerEtatParcelle() {
		super(EtatParcelle.class);
	}

	@Override
	protected void prepareNewObject(EtatParcelle object) {
		object.setActif(true);
		object.setLocked(false);
		object.setACorriger(false);
	}
}
