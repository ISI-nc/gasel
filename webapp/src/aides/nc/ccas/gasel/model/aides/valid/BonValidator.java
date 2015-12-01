package nc.ccas.gasel.model.aides.valid;

import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.validator.GaselValidator;

public class BonValidator extends GaselValidator<Bon> {

	public BonValidator() {
		super(Bon.class);
	}

	@Override
	public void validateImpl(Bon object) {
		validateDataObject(object);
		if (object.getUsage() != null) {
			subValidate("Usage", object.getUsage());
		}
	}

}
