package nc.ccas.gasel.pages.habitat;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.habitat.ActionSociale;

public abstract class EditActionSociale extends EditPage<ActionSociale> {

	
	public EditActionSociale() {
		super(ActionSociale.class);
	}
	
	public void ajouterObjectif() {
		((EditerObjectif) getRequestCycle().getPage(
				"habitat/EditerObjectif")).activateForParent(getObject(),
				"actionSociale");
	}
}
