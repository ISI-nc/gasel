package nc.ccas.gasel.model.budget.valid;

import nc.ccas.gasel.collections.Sort;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.mairie.FournisseurMairie;
import nc.ccas.gasel.validator.GaselValidator;

public class NatureAideValidator extends GaselValidator<NatureAide> {

	public NatureAideValidator() {
		super(NatureAide.class);
	}

	@Override
	protected void validateImpl(NatureAide nature) {
		checkUnicity(nature, "parent", nature.getParent(), "code", nature
				.getCode());

		for (FournisseurMairie fournisseur : new Sort<FournisseurMairie>(nature
				.getFournisseurs()).by("libelle").results()) {
			if (!fournisseur.isActif()) {
				warning("Fournisseur inactif : " + fournisseur.getLibelle());
			}
		}
	}

}
