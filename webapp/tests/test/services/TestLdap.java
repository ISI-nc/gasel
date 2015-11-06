package test.services;

import junit.framework.TestCase;
import nc.ccas.gasel.services.AuthLDAP;
import nc.ccas.gasel.services.AuthLDAPImpl;
import nc.ccas.gasel.services.AuthResult;

public class TestLdap extends TestCase {

	private static final String[] LOGINS = new String[] { //
			"admin", "nimportequoi",
			"administrateur", //
			"boulu72", "samla75", "gragh66", "cerad73", "morsa76", "attka71",
			"regan76", "petso68", "evase73", "fullo56", "deran84", "chaka64",
			"blami70", "brina73", "lecva70", "brufl78", "checa75", "faila74",
			"gauma77", "inhfa74", "lavna64", "monvi82", "niuli83", "soeph77",
			"davin71", "garma82", "laumi57", "tarhe80", "reigw85", "garca72",
			"tamma78", "buqch62", "soufl76" };

	private final AuthLDAP auth = new AuthLDAPImpl();

	public void testInexistantUser() throws Exception {
		assertFalse(auth.authenticate("existepas", "dutout").isValid());
	}
	
	public void testWrongPassword() throws Exception {
		for (String login : LOGINS) {
			assertFalse(auth.authenticate(login, "wrongpass").isValid());
		}
	}

	public void testWorking() throws Exception {
		AuthResult result = auth.authenticate("isinc", "P@ssw0rd");
		assertTrue(result.isValid());
		assertEquals("isi", result.getNom());
		assertEquals("nc", result.getPrenom());
	}

}
