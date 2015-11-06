package test.services.doc;

import junit.framework.TestCase;
import nc.ccas.gasel.services.doc.XmlDocChanger;

public class TestXmlDocChanger extends TestCase {

	public void testIdentityChange() throws Exception {
		String doc = "text1 ${var1} text2 ${var2} text3";

		String result = new XmlDocChanger(doc) //
				.subst("var1", "${var1}") //
				.subst("var2", "${var2}") //
				.result();

		assertEquals(doc, result);
	}

	public void testRemoveVar() throws Exception {
		String doc = "${var}";
		String result = new XmlDocChanger(doc).subst("var", "").result();

		assertTrue(result.length() == 0);
	}

	public void testSubstVar() throws Exception {
		String doc = "${var}";
		String result = new XmlDocChanger(doc).subst("var", "value").result();

		assertEquals("value", result);
	}

}
