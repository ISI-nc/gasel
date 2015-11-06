package nc.ccas.gasel.pages.parametres.pe;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pe.enums.Formation;

public abstract class FormationEdit extends EditPage<Formation> {
	
	public FormationEdit() {
		super(Formation.class);
	}
	
	@Override
	protected void prepareNewObject(Formation object) {
		object.setActif(true);
		object.setLocked(false);
	}
}
