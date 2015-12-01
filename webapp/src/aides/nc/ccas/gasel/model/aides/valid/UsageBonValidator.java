package nc.ccas.gasel.model.aides.valid;

import nc.ccas.gasel.model.aides.UsageBon;
import nc.ccas.gasel.validator.GaselValidator;

public class UsageBonValidator extends GaselValidator<UsageBon> {
	
	public UsageBonValidator() {
		super(UsageBon.class);
	}
	
	@Override
	public void validateImpl(UsageBon object) {
		validateDataObject(object);
		if (object.getBon().getMontant() < object.getMontantUtilise()) {
			error("Le montant utilisé dépasse le montant du bon !");
		}
	}

}
