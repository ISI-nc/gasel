package test.model.valid;

import static org.apache.cayenne.DataObjectUtils.objectForPK;
import junit.framework.TestCase;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.validator.GaselValidator;

import org.apache.cayenne.access.DataContext;

public class TestPersonneValidator extends TestCase {

	private GaselValidator<Personne> validator;

	private DataContext dc;

	@Override
	protected void setUp() throws Exception {
		validator = GaselValidator.validatorFor(Personne.class);
		dc = CayenneUtils.createDataContext();
	}

	public void testEmptyObject() {
		Personne p = (Personne) dc.newObject(Personne.class);
		validator.validate(p);
		assert !validator.getErrors().isEmpty();
	}

	public void testExistingObject() {
		Personne p = (Personne) objectForPK(dc, Personne.class, 1);
		validator.validate(p);
		assert validator.getErrors().isEmpty();
	}

}
