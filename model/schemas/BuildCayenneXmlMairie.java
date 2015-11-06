import java.io.FileOutputStream;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;

public class BuildCayenneXmlMairie {

	public static void main(String[] args) throws Exception {
		Document doc = new Builder().build(MyGen.DEST_DIR + "/cayenne.xml");

		Element dataNode = doc.getRootElement().getFirstChildElement("domain")
				.getFirstChildElement("node");

		dataNode.getAttribute("datasource").setValue("x");
		dataNode.getAttribute("factory").setValue(
				"nc.ccas.gasel.cayenne.MairieDSF");

		FileOutputStream out = new FileOutputStream("mairie/cayenne.xml");
		out.write(doc.toXML().getBytes());
		out.close();
	}

}
