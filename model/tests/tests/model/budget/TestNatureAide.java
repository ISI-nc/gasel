package tests.model.budget;

import junit.framework.TestCase;
import nc.ccas.gasel.model.budget.NatureAide;
import tests.model.AllModelTests;

/**
 * Tests sur les vues du budget.
 * 
 * @author isinc
 */
public class TestNatureAide extends TestCase {

	static {
		AllModelTests.setupDatabase();
	}

	public void testIsEauWord() throws Exception {
		NatureAide na = new NatureAide();
		na.setLibelle("Eau");
		assertTrue(na.isEau());
	}

	public void testIsEauDebut() throws Exception {
		NatureAide na = new NatureAide();
		na.setLibelle("Eau (bon)");
		assertTrue(na.isEau());
	}

	public void testIsEauFin() throws Exception {
		NatureAide na = new NatureAide();
		na.setLibelle("2014 Eau");
		assertTrue(na.isEau());
	}

	public void testIsNotEau() throws Exception {
		NatureAide na = new NatureAide();
		na.setLibelle("sflk eau jhgd");
		assertFalse(na.isEau());
	}
}
