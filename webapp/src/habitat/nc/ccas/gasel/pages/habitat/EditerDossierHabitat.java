package nc.ccas.gasel.pages.habitat;

import java.util.List;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.habitat.AspectDossierHabitat;
import nc.ccas.gasel.model.habitat.DemandeAideLogement;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.IPropertySelectionModel;

public abstract class EditerDossierHabitat extends
		EditPage<AspectDossierHabitat> {

	public EditerDossierHabitat() {
		super(AspectDossierHabitat.class);
	}

	@Persist("workflow")
	public abstract DemandeAideLogement getDemandeAideLogement();

	public void ajouterDemandeAideLogement() {
		((EditDemandeAideLogement) getRequestCycle().getPage(
				"habitat/EditDemandeAideLogement")).activateForParent(
				getObject(), "dossierHabitat");
	}

	public void ajouterActionSociale() {
		((EditActionSociale) getRequestCycle().getPage(
				"habitat/EditActionSociale")).activateForParent(getObject(),
				"dossier");
	}

	public void ajouterDemandeAffectation() {
		((EditDemandeAffectation) getRequestCycle().getPage(
				"habitat/EditDemandeAffectation")).activateForParent(
				getObject(), "dossier");
	}

	@Override
	protected void prepareEnregistrer() {
		AspectDossierHabitat doss = getObject();
		if (doss.getEcheanceSortie() == null && doss.getDebutASL() != null) {
			doss.setEcheanceSortie(DateUtils.addYears(doss.getDebutASL(), 3));
		}
	}

	public IPropertySelectionModel getTisfModel() {
		List<Utilisateur> utilisateurs = sort(
				sql.query().all(Utilisateur.class)).byToString().results();
		return psm.withNull(psm.dataObject(utilisateurs));
	}

}
