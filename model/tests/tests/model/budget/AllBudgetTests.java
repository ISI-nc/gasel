package tests.model.budget;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllBudgetTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests budget");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestLigneVirement.class);
		//$JUnit-END$
		return suite;
	}

}
