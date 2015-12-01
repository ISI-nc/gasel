package nc.ccas.gasel.model.pe.valid;

import java.util.Date;

import nc.ccas.gasel.model.pe.AssistanteMaternelle;
import nc.ccas.gasel.validator.GaselValidator;

public class AssistanteMaternelleValidator extends
		GaselValidator<AssistanteMaternelle> {

	public AssistanteMaternelleValidator() {
		super(AssistanteMaternelle.class);
	}

	@Override
	protected void validateImpl(AssistanteMaternelle am) {
		validateDataObject(am);

		Date dans3mois = nc.ccas.gasel.modelUtils.DateUtils.nMoisApres(3);
		warning("L'assurance logement expire dans moins de 3 mois",
				checkDateBefore(am.getAssuranceLogement(), dans3mois));
		warning("L'assurance véhicule expire dans moins de 3 mois",
				checkDateBefore(am.getAssuranceVehicule(), dans3mois));
	}

}
