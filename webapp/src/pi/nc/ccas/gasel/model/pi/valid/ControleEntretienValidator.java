package nc.ccas.gasel.model.pi.valid;

import nc.ccas.gasel.model.pi.ControleEntretien;
import nc.ccas.gasel.validator.GaselValidator;

public class ControleEntretienValidator extends
		GaselValidator<ControleEntretien> {

	public ControleEntretienValidator() {
		super(ControleEntretien.class);
	}

	@Override
	protected void validateImpl(ControleEntretien object) {
		validateDataObject(object);

		if (object.getDate() != null
				&& dates.checkFuture(object.getDate(), true))
			error("Date dans le futur !");
	}

}
