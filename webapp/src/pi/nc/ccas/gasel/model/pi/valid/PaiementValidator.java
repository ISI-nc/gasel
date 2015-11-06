package nc.ccas.gasel.model.pi.valid;

import nc.ccas.gasel.model.pi.AttributionJF;
import nc.ccas.gasel.model.pi.Paiement;
import nc.ccas.gasel.validator.GaselValidator;

public class PaiementValidator extends GaselValidator<Paiement> {

	public PaiementValidator() {
		super(Paiement.class);
	}

	@Override
	protected void validateImpl(Paiement object) {
		validateDataObject(object);

		if (dates.checkRecouvrement(object, object.getAttribution()
				.getPaiements())) {
			error("Recouvrement avec un autre paiement.");
		}

		if (object.getAttribution() != null) {
			AttributionJF attribution = object.getAttribution();

			if (attribution.getPremierArrete() != null
					&& object.getDebut() != null
					&& object.getDebut().before(
							attribution.getPremierArrete().getDebut())) {
				error("Paiement avant le début du premier arrêté.");
			}

			if (attribution.getDernierArrete() != null
					&& object.getFin() != null
					&& object.getFin().after(
							attribution.getDernierArrete().getFin())) {
				error("Paiement après la fin du dernier arrêté.");
			}
		}

	}

}
