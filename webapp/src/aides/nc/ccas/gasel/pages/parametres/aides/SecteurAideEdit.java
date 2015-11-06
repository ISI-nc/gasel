package nc.ccas.gasel.pages.parametres.aides;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.budget.SecteurAide;

public abstract class SecteurAideEdit extends EditPage<SecteurAide> {

	public SecteurAideEdit() {
		super(SecteurAide.class);
	}

	@Override
	protected void prepareNewObject(SecteurAide object) {
		object.setActif(true);
		object.setLocked(false);
	}

}
