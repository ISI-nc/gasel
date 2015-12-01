package nc.ccas.gasel.pages.parametres.pe;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pe.enums.AutorisationParentale;

public abstract class AutorisationParentaleEdit extends
		EditPage<AutorisationParentale> {

	protected AutorisationParentaleEdit() {
		super(AutorisationParentale.class);
	}

	@Override
	protected void prepareNewObject(AutorisationParentale object) {
		object.setActif(true);
		object.setLocked(false);
	}
}
