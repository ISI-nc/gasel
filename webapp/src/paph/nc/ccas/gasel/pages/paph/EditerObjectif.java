package nc.ccas.gasel.pages.paph;

import java.util.ArrayList;
import java.util.List;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.core.enums.Problematique;
import nc.ccas.gasel.model.core.enums.TypeProblematique;
import nc.ccas.gasel.model.paph.ObjectifPAPH;
import nc.ccas.gasel.model.paph.ObjectifProbPAPH;

import org.apache.tapestry.annotations.Persist;

public abstract class EditerObjectif extends EditPage<ObjectifPAPH> {

	public EditerObjectif() {
		super(ObjectifPAPH.class);
	}

	@Persist("workflow")
	public abstract TypeProblematique getType();

	@Persist("workflow")
	public abstract Problematique getProblematique();

	public List<Problematique> getProblematiquesDispo() {
		List<Problematique> lp = new ArrayList<Problematique>();
		if (getObject().getAccompagnement() != null) {
			for (Problematique prob : getObject().getAccompagnement()
					.getDossier().getDossier().getDossier().getProblematiques()) {
				if (getObject().getProblematiques().contains(prob))
					continue;
				if (prob.getParent().equals(getType())) {
					lp.add(prob);
				}
			}
		}
		if (lp.size() != 0)
			return lp;
		return null;
	}

	public void editerReponse() {
		((EditerReponse) getRequestCycle().getPage("paph/EditerReponse"))
				.activateForParent(getObject(), "objectif");
	}

	public void ajouterProblematique() {
		ObjectifProbPAPH objectif = createDataObject(ObjectifProbPAPH.class);
		objectif.setProblematique(getProblematique());
		getObject().addToProblematiques(objectif);
		redirect();
	}

}
