package nc.ccas.gasel.model.core.valid;

import nc.ccas.gasel.model.core.Adresse;
import nc.ccas.gasel.validator.GaselValidator;

public class AdresseValidator extends GaselValidator<Adresse> {

	public AdresseValidator() {
		super(Adresse.class);
	}

	@Override
	protected void validateImpl(Adresse adresse) {
		validateDataObject(adresse);
		if (adresse.getBoitePostale() == null && adresse.getAutres() == null) {
			if (adresse.getNumero() == null || adresse.getRue() == null) {
				error("Il faut une boite postale, un numéro et une rue, ou le champ autre.");
			}
		}
		if (adresse.getAutres() == null) {
			require(true, adresse.getVille(), "ville");
		}
	}

}
