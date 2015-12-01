package nc.ccas.gasel.model.habitat.valid;

import nc.ccas.gasel.model.habitat.DemandeAffectation;
import nc.ccas.gasel.model.habitat.enums.TypeAffDemandeHabitat;
import nc.ccas.gasel.validator.GaselValidator;

public class DemandeAffectationValidator extends
		GaselValidator<DemandeAffectation> {

	public DemandeAffectationValidator() {
		super(DemandeAffectation.class);
	}

	@Override
	protected void validateImpl(DemandeAffectation demande) {
		validateDataObject(demande);

		if (demande.getTypeDemande() != null) {
			switch (demande.getTypeDemande().getId()) {
			case TypeAffDemandeHabitat.LOCATIF:
				requireProperty(true, demande, "affLocatif");
				break;
			case TypeAffDemandeHabitat.REHABILITATION:
				requireProperty(true, demande, "affRehabilitation");
				break;
			case TypeAffDemandeHabitat.ACCESSION:
				requireProperty(true, demande, "affAccession");
				break;
			}
		}
	}

}
