package nc.ccas.gasel.pages.parametres.aides;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.aides.TypeArrete;

public abstract class TypeArreteEdit extends EditPage<TypeArrete> {

	public TypeArreteEdit() {
		super(TypeArrete.class);
	}

	@Override
	protected void prepareNewObject(TypeArrete object) {
		object.setActif(true);
		object.setLocked(false);
	}

}
