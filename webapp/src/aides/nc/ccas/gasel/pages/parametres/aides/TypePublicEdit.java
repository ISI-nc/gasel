package nc.ccas.gasel.pages.parametres.aides;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.core.enums.TypePublic;

public abstract class TypePublicEdit extends EditPage<TypePublic> {

	public TypePublicEdit() {
		super(TypePublic.class);
	}

	@Override
	protected void prepareNewObject(TypePublic object) {
		object.setActif(true);
		object.setLocked(false);
	}

}
