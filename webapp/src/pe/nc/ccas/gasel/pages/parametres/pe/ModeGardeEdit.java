package nc.ccas.gasel.pages.parametres.pe;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pe.enums.ModeGarde;

public abstract class ModeGardeEdit extends EditPage<ModeGarde> {
	
	public ModeGardeEdit() {
		super(ModeGarde.class);
	}
	
	@Override
	protected void prepareNewObject(ModeGarde object) {
		object.setActif(true);
		object.setLocked(false);
	}

}
