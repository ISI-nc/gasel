package nc.ccas.gasel.pages.habitat;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.habitat.ObjectifHabitat;

public abstract class EditerObjectif extends EditPage<ObjectifHabitat> {
	
	public EditerObjectif() {
		super(ObjectifHabitat.class);
	}
	
	public void ajouterProblematique() {
		((EditerProblematique) getRequestCycle().getPage(
				"habitat/EditerProblematique")).activateForParent(getObject(),
				"objectif");
	}

}
