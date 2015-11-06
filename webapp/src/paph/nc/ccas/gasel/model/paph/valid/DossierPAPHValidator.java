package nc.ccas.gasel.model.paph.valid;

import nc.ccas.gasel.model.paph.DossierPAPH;
import nc.ccas.gasel.validator.GaselValidator;

public class DossierPAPHValidator extends GaselValidator<DossierPAPH> {

	public DossierPAPHValidator() {
		super(DossierPAPH.class);
	}

	@Override
	public void validateImpl(DossierPAPH paph) {
		validateDataObject(paph);
	}

}
