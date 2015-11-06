package nc.ccas.gasel.pages.habitat;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.habitat.ActionCollective;
import nc.ccas.gasel.model.habitat.AspectDossierHabitat;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;

public abstract class EditerActionCollective extends EditPage<ActionCollective> {
	
	public EditerActionCollective() {
		super(ActionCollective.class);
	}

	public void delDossierAttache(AspectDossierHabitat dossier) {
		getObject().removeFromDossiers(dossier);
		redirect(getRequestCycle());
	}
	
	@InjectPage("habitat/DossierHabitatSearch")
	public abstract DossierHabitatSearch getDossierHabitatSearchPage();

	public void ajouterDossierAttache(IRequestCycle cycle) {
		getDossierHabitatSearchPage().activate(this, "retourAjoutDossierHabitat");
	}
	
	public void retourAjoutDossierHabitat(AspectDossierHabitat dossier) {
		getObject().addToDossiers(dossier);
	}
	
}
