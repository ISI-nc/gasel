package test.services.doc;

import static java_gaps.ResourceUtils.readResource;

import java.io.IOException;
import java.util.Set;

import junit.framework.TestCase;
import nc.ccas.gasel.services.doc.DocumentCheckResult;
import nc.ccas.gasel.services.doc.XmlDocChecker;

public class TestXmlDocChecker extends TestCase {

	private XmlDocChecker checker = new XmlDocChecker();

	public void testCheckOk() throws Exception {
		DocumentCheckResult checkResult = checkDoc("xml_doc_sample.xml",
				"var1", "var2");

		assertTrue(checkResult.errors().isEmpty());
		assertTrue(checkResult.warnings().isEmpty());

		Set<String> vars = checkResult.variables();
		assertEquals(2, vars.size());
		assertTrue(vars.contains("var1"));
		assertTrue(vars.contains("var2"));
	}

	public void testCheckUnknownVars() throws Exception {
		DocumentCheckResult checkResult = checkDoc("xml_doc_sample.xml", "var1");

		assertTrue(checkResult.errors().contains("Variable invalide : var2"));
	}

	public void testCheckUnsuedVars() throws Exception {
		DocumentCheckResult checkResult = checkDoc("xml_doc_sample.xml",
				"var1", "var2", "var3");

		assertTrue(checkResult.warnings()
				.contains("Variable inutilisée : var3"));
	}

	public void testCheckInvalidDocument() throws Exception {
		DocumentCheckResult checkResult = checkDoc("incorrect_document_sample.bad_xml");
		assertTrue(checkResult.errors().contains("Format du document invalide"));
	}

	private DocumentCheckResult checkDoc(String fileName,
			String... allowedVariables) throws IOException {
		String documentData = readResource(TestXmlDocChecker.class, fileName);
		return checker.checkDocument(documentData, allowedVariables);
	}

}
