package test.services.doc;

import junit.framework.TestCase;
import nc.ccas.gasel.services.doc.DocumentCheckResult;

public class TestCheckResult extends TestCase {

	private DocumentCheckResult checkResult = new DocumentCheckResult();

	public void testErrors() throws Exception {
		checkResult.error("x");
		assertTrue(checkResult.errors().contains("x"));
	}

	public void testWarnings() throws Exception {
		checkResult.warning("x");
		assertTrue(checkResult.warnings().contains("x"));
	}

	public void testVariables() throws Exception {
		checkResult.variable("x");
		assertTrue(checkResult.variables().contains("x"));
	}

}
