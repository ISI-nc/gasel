package test.model.aides.valid;

import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.modelUtils.DateUtils;
import test.model.valid.ValidatorTest;

public class TestAideValidator extends ValidatorTest<Aide> {

	public TestAideValidator() {
		super(Aide.class);
	}

	public void testAideOccasionnelle() {
		Aide aide = create(Aide.class);
		aide
				.setStatut(get(StatutAide.class,
						StatutAide.OCCASIONNELLE));

		aide.setDebut(DateUtils.today());
		aide.setFin(DateUtils.finMois());

		validator.validate(aide);
		assertError("ne peut commencer dans le mois");

		aide.setDebut(DateUtils.nMoisApres(1));
		aide.setFin(DateUtils.finMois(aide.getDebut()));
		validator.clear();
		validator.validate(aide);
		assertNoError("ne peut commencer dans le mois");
	}

}
