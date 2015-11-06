package nc.ccas.gasel.model.pi.valid;

import nc.ccas.gasel.model.pi.ArreteJF;
import nc.ccas.gasel.validator.GaselValidator;

public class ArreteJFValidator extends GaselValidator<ArreteJF> {

	public ArreteJFValidator() {
		super(ArreteJF.class);
	}

	@Override
	protected void validateImpl(ArreteJF object) {
		validateDataObject(object);

		warning("Pas de numéro", object.getNumero() == null);

		if (dates.checkRecouvrement(object, object.getAttribution()
				.getArretes())) {
			error("Recouvrement avec un autre arrêté de cette attribution");
		} else if (dates.checkRecouvrement(object, object
				.getArretesDeLaMemeParcelle())) {
			error("Recouvrement avec un autre arrêté d'une autre attribution");
		}
	}

}
