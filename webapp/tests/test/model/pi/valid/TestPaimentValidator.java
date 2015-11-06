package test.model.pi.valid;

import java.util.Date;

import nc.ccas.gasel.model.pi.AttributionJF;
import nc.ccas.gasel.model.pi.Paiement;
import test.model.valid.ValidatorTest;

public class TestPaimentValidator extends ValidatorTest<Paiement> {

	public TestPaimentValidator() {
		super(Paiement.class);
	}

	@SuppressWarnings("deprecation")
	public void testRecouvrement() {
		AttributionJF attribution = create(AttributionJF.class);

		Paiement p1 = create(Paiement.class);
		attribution.addToPaiements(p1);
		p1.setDebut(new Date(8, 1, 1));
		p1.setFin(new Date(8, 1, 20));

		Paiement p2 = create(Paiement.class);
		attribution.addToPaiements(p2);
		p2.setDebut(new Date(8, 0, 10));
		p2.setFin(new Date(8, 1, 15));
		

		validator.validate(p1);
		assertError("Recouvrement");

		validator.clear();
		p2.setFin(new Date(8, 0, 20));
		validator.validate(p1);
		assertNoError("Recouvrement");
	}

}
