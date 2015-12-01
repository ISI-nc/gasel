package test.io;

import junit.framework.TestCase;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.services.DataObjectSqueezer;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.DataObjectUtils;
import org.apache.tapestry.util.io.DataSqueezerImpl;
import org.apache.tapestry.util.io.IntegerAdaptor;

public class DataObjectSqueezerTest extends TestCase {

	private static final String EXPECTED = "QUtilisateur:1";

	private DataContext dc;
	private DataSqueezerImpl squeezer;

	@Override
	protected void setUp() throws Exception {
		dc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(dc);

		squeezer = new DataSqueezerImpl();
		squeezer.register(new DataObjectSqueezer());
		squeezer.register(new IntegerAdaptor());
	}

	public void testSqueeze() {
		Utilisateur u = (Utilisateur) DataObjectUtils.objectForPK(dc,
				Utilisateur.class, 1);

		String s = squeezer.squeeze(u);
		assertEquals(EXPECTED, s);
	}

	public void testUnsqueeze() {
		Utilisateur u = (Utilisateur) squeezer.unsqueeze(EXPECTED);
		assertNotNull(u);
	}

}
