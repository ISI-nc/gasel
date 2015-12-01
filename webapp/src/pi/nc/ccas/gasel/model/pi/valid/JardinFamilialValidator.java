package nc.ccas.gasel.model.pi.valid;

import nc.ccas.gasel.model.pi.JardinFamilial;
import nc.ccas.gasel.validator.GaselValidator;

public class JardinFamilialValidator extends GaselValidator<JardinFamilial> {

	public JardinFamilialValidator() {
		super(JardinFamilial.class);
	}

	@Override
	protected void validateImpl(JardinFamilial jardin) {
		validateDataObject(jardin);

		error("Il existe déjà un jardin avec ce nom", !checkUnicity(jardin,
				"nom", jardin.getNom()));
	}
}
