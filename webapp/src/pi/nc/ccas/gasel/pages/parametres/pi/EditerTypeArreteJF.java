package nc.ccas.gasel.pages.parametres.pi;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pi.enums.TypeArreteJF;

public abstract class EditerTypeArreteJF extends EditPage<TypeArreteJF> {

	public EditerTypeArreteJF() {
		super(TypeArreteJF.class);
	}

	@Override
	protected void prepareNewObject(TypeArreteJF object) {
		object.setActif(true);
		object.setLocked(false);
		object.setRenouvellement(false);
	}

}
