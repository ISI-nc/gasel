package tests.modelUtils;

import static nc.ccas.gasel.modelUtils.DateUtils.intersection;
import junit.framework.TestCase;

public class TestDateUtils extends TestCase {

	public void testIntersection() {
		// -- Cas vrais --

		// L'un dans l'autre
		rangeTest(true, 1, 20, 10, 15);
		// Interection par un coté
		rangeTest(true, 10, 15, 10, 20);
		// Interection par la limite
		rangeTest(true, 10, 20, 20, 30);
		// Tout ouvert
		rangeTest(true, null, null, null, null);
		// Un ouvert
		rangeTest(true, 1, 10, null, null);
		// Demi-ouvert
		rangeTest(true, 1, null, null, 10);
		// Demi-ouvert par la limite
		rangeTest(true, 1, null, null, 1);

		// -- Cas faux --
		// Pas d'intersection
		rangeTest(false, 1, 5, 6, 10);
		// Demi-ouvert sans intersection
		rangeTest(false, null, 1, 10, null);
	}

	private void rangeTest(boolean expect, Integer d1, Integer f1, Integer d2,
			Integer f2) {
		assertEquals(expect, intersection(d1, f1, d2, f2));
		assertEquals(expect, intersection(d2, f2, d1, f1));
	}

}
