package nc.ccas.gasel.jwcs.dossiers;

import java.util.ArrayList;
import java.util.List;

import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.enums.Problematique;
import nc.ccas.gasel.model.core.enums.TypeProblematique;

import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.Persist;

public abstract class Problematiques extends BaseComponent {

	@Parameter(required = true)
	public abstract Dossier getDossier();

	@Persist("workflow")
	public abstract TypeProblematique getType();

	public abstract void setType(TypeProblematique typ);

	@Persist("workflow")
	public abstract Problematique getProblematique();

	public abstract void setProblematique(Problematique problematique);

	public void ajouterProblematique() {
		getDossier().addToProblematiques(getProblematique());
		setProblematique(null);
		reloadPage();
	}

	public TypeProblematique getTypeFacade() {
		return getType();
	}

	public void setTypeFacade(TypeProblematique type) {
		setType(type);
		if (type == null
				|| !type.getProblematiques().contains(getProblematique())) {
			setProblematique(null);
		}
	}

	public List<Problematique> filtrer(List<Problematique> probs) {
		List<Problematique> retval = new ArrayList<Problematique>(probs.size());
		List<Problematique> probsDossier = getDossier().getProblematiques();
		for (Problematique prob : probs) {
			if (probsDossier.contains(prob))
				continue;
			retval.add(prob);
		}
		return retval;
	}
}
