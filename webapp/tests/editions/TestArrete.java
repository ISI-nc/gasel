package editions;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;
import nc.ccas.gasel.SqlParams;
import nc.ccas.gasel.model.aides.Arrete;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.starjet.aides.ArreteFo;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;


public class TestArrete extends TestCase {

	@Override
	protected void setUp() throws Exception {
	}

	private ArreteFo fo = new ArreteFo();

	private FopFactory fopFactory = FopFactory.newInstance();

	private TransformerFactory trFactory = TransformerFactory.newInstance();

	public void testEau() throws Exception {
		doOutput("Arrete_EAU", "2008/74");
	}

	public void testOM() throws Exception {
		doOutput("Arrete_OM", "2008/80");
	}

	public void testAvance() throws Exception {
		doOutput("Arrete_AVANCE", "2008/10");
	}

	public void testSolidarite() throws Exception {
		doOutput("Arrete_SOLIDARITE", "2009/38");
	}

	@SuppressWarnings("unchecked")
	private void doOutput(String fichier, String numero)
			throws FileNotFoundException, FOPException,
			TransformerConfigurationException, TransformerException,
			IOException {
		Expression expr;
		// expr = Expression.fromString("type=$type") //
		// .expWithParameters(new SqlParams().set("type", TYPE));
		expr = Expression.fromString("numero = $numero") //
				.expWithParameters(new SqlParams().set("numero", numero));

		DataContext context = CayenneUtils.createDataContext();
		SelectQuery query = new SelectQuery(Arrete.class, expr);
		query.addOrdering("creation", false);
		query.setFetchLimit(1);

		List<Arrete> select = context.performQuery(query);
		if (select.isEmpty()) {
			throw new RuntimeException("Pas d'arrête n°" + numero);
		}
		Arrete arrete = select.get(0);

		String foDoc = fo.writeXslFo(arrete);

		OutputStream out;

		out = new FileOutputStream(fichier + ".xml");
		out.write(foDoc.getBytes());
		out.close();

		out = new BufferedOutputStream(new FileOutputStream(fichier + ".pdf"));

		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

		Transformer transformer = trFactory.newTransformer();
		Source source = new StreamSource(new StringReader(foDoc));
		Result result = new SAXResult(fop.getDefaultHandler());
		transformer.transform(source, result);

		out.close();
	}
}
