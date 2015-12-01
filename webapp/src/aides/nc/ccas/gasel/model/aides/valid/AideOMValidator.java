package nc.ccas.gasel.model.aides.valid;

import nc.ccas.gasel.model.aides.AideOM;
import nc.ccas.gasel.validator.GaselValidator;

public class AideOMValidator extends GaselValidator<AideOM> {
	
	public AideOMValidator() {
		super(AideOM.class);
	}
	
	@Override
	protected void validateImpl(AideOM om) {
		checkDataObjectValidation(om);
		if (om.getNumeroRedevable() != null && om.getNumeroRedevable().length() > 5) {
			error("le numéro de redevable est limité à 5 caractères");
		}
	}

}
