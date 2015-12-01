package tests.model.core.docs;

import static java_gaps.ResourceUtils.readResource;

import java.io.IOException;

import junit.framework.TestCase;
import nc.ccas.gasel.model.core.docs.ModeleDocument;

public class TestModeleDocument extends TestCase {

	public void testDataTransformationReversible() throws Exception {
		String testString = "test àéè\1\2";

		ModeleDocument m = new ModeleDocument();

		m.setData(testString);

		assertEquals(testString, m.getData());
	}

	public void testOnRealData() throws Exception {
		for (int i = 1; i <= 2; i++) {
			String data = readDoc(i);

			ModeleDocument m = new ModeleDocument();
			m.setData(data);

			String gzipedData = (String) m.readProperty("data");
			System.out.printf("Ratio: %2d%% | %.2fko -> %.2fko\n", //
					gzipedData.length() * 100 / data.length(), //
					data.length() / 1024d, gzipedData.length() / 1024d);

			assertEquals(data, m.getData());
		}
	}

	private String readDoc(int num) throws IOException {
		return readResource(getClass(), "xml_doc_sample_" + num + ".xml");
	}

}
