package test.services;

import static nc.ccas.gasel.modelUtils.CayenneUtils.collectIds;
import static nc.ccas.gasel.modelUtils.CommonQueries.select;
import static nc.ccas.gasel.modelUtils.CommonQueries.unique;
import static org.apache.cayenne.DataObjectUtils.intPKForObject;
import static org.apache.cayenne.access.DataContext.createDataContext;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nc.ccas.gasel.model.aides.Arrete;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.services.reports.ReportService;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperRunManager;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.TestBase;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.services.ServiceMap;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebContext;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;
import org.apache.tapestry.web.WebSession;
import org.testng.annotations.Test;

import test.FakeWebSession;

public class TestReportService extends TestBase {

	private static final String TEST_PATH = "test/services/test.jasper";

	private static final String ARRETE = "2014/29";
	//private static final String ARRETE = "2015/test/001";

	public static void main(String[] args) throws Exception {
		TestReportService test = new TestReportService();
		test.testArreteGenerique();
	}

	@Test
	public void testSimpleRender() throws Exception {
		InputStream reportAsStream = getClass().getClassLoader()
				.getResourceAsStream(TEST_PATH);

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		JasperRunManager.runReportToPdfStream(reportAsStream, output,
				new TreeMap<String, Object>(), new JREmptyDataSource());
	}

	@Test
	public void testRenderBon() throws Exception {
		doReportTest("edition-bons", "pdf", "BONS_ID", Collections.emptyList());

		List<Bon> values = select(Bon.class, "numero like '201403000%'");
		List<Bon> slice = new ArrayList<>();
		for (int i = 0; i < 4 && i < values.size(); i++) {
			slice.add(values.get(i));
		}
		assertNotEquals(0, slice.size());
		doReportTest("edition-bons", "pdf", "BONS_ID", collectIds(slice));
	}

	@Test
	public void testArreteAlimentation() throws Exception {
		testArrete("arrete-alimentation");
	}

	@Test
	public void testArreteSolidarite() throws Exception {
		testArrete("arrete-solidarite");
	}

	@Test
	public void testArreteAvance() throws Exception {
		testArrete("arrete-avance");
	}

	@Test
	public void testArreteOM() throws Exception {
		testArrete("arrete-om");
	}

	@Test
	public void testArreteCde() throws Exception {
		testArrete("arrete-cde");
	}

	@Test
	public void testArreteEEC() throws Exception {
		testArrete("arrete-eec");
	}

	@Test
	public void testArreteUrgences() throws Exception {
		testArrete("arrete-urgences");
	}

	@Test
	public void testArreteGenerique() throws Exception {
		 testArrete("arrete-generique");
		 testArrete("arrete-generique-sans-factures");
	}

	private void testArrete(String view) throws IOException,
			FileNotFoundException {
		testArrete(view, ARRETE);
	}

	private void testArrete(String view, String numero) throws IOException,
			FileNotFoundException {
		Arrete arrete = unique(createDataContext(), Arrete.class, //
				"numero", numero);
		System.out.println(view + " for " + arrete);
		doReportTest(view, "pdf", "ARRETE_ID", intPKForObject(arrete));
//		for (String format : ReportService.getSupportedFormats()) {
//			doReportTest(view, format, "ARRETE_ID", intPKForObject(arrete));
//		}
	}

	private void doReportTest(String reportView, String format, String key,
			Object value) throws IOException, FileNotFoundException {
		IRequestCycle cycle = cycleMock();

		try {
			Map<String, Object> parameters = new TreeMap<>();
			parameters.put(ReportService.FORMAT_PARAM, format);
			parameters.put(key, value);
			parameters.put("titre", "TITRE TEST");
			parameters.put("article1", "Article 1 text...:");
			parameters.put("infosImputation",
					"Info imputation\n- test\n- fonction: test");
			parameters.put("avecFactures", true);
			ReportService.invoke(cycle, reportView, parameters);
			unreachable();
		} catch (RedirectException ex) {
			String sessionKey = ex.getRedirectLocation();

			// simulate parameter
			when(cycle.getParameter(ServiceConstants.PARAMETER)).thenReturn(
					sessionKey);

			// simulate unsqueezing
			DataSqueezer squeezer = mock(DataSqueezer.class);
			when(cycle.getInfrastructure().getDataSqueezer()).thenReturn(
					squeezer);
			when(squeezer.unsqueeze(sessionKey)).thenReturn(sessionKey);
		}

		// Capture the output
		ContentType contentType = ReportService.getSupportForExtension(format)
				.getContentType();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		when(
				cycle.getInfrastructure().getResponse()
						.getOutputStream(contentType)).thenReturn(output);

		cycle.getInfrastructure().getServiceMap().getService("reports")
				.service(cycle);

		// Record output for control
		FileOutputStream out = new FileOutputStream("test-output/" + reportView
				+ "." + format);
		output.writeTo(out);
		out.close();
	}

	private IRequestCycle cycleMock() {
		IRequestCycle cycle = mock(IRequestCycle.class);

		Infrastructure infrastructure = mock(Infrastructure.class);
		when(cycle.getInfrastructure()).thenReturn(infrastructure);

		WebRequest request = mock(WebRequest.class);
		when(infrastructure.getRequest()).thenReturn(request);

		WebResponse response = mock(WebResponse.class);
		when(infrastructure.getResponse()).thenReturn(response);

		WebSession session = new FakeWebSession();
		when(request.getSession(true)).thenReturn(session);
		when(request.getSession(false)).thenReturn(session);

		ServiceMap serviceMap = mock(ServiceMap.class);
		when(infrastructure.getServiceMap()).thenReturn(serviceMap);

		WebContext webContext = mock(WebContext.class);
		when(webContext.getRealPath("/WEB-INF/reports")).thenReturn(
				new File("src/main/webapp/WEB-INF/reports").getAbsolutePath());

		ReportService reportService = new ReportService();
		reportService.setContext(webContext);
		when(serviceMap.getService("reports")).thenReturn(reportService);

		LinkFactory linkFactory = new LinkFactory() {
			@Override
			public ServiceEncoder[] getServiceEncoders() {
				return null;
			}

			@Override
			public Object[] extractListenerParameters(IRequestCycle arg0) {
				return null;
			}

			@SuppressWarnings("rawtypes")
			@Override
			public ILink constructLink(IEngineService arg0, boolean arg1,
					Map arg2, boolean arg3) {
				ILink link = mock(ILink.class);
				when(link.getURL()).thenReturn(
						(String) ((Object[]) arg2
								.get(ServiceConstants.PARAMETER))[0]);
				return link;
			}
		};

		reportService.setLinkFactory(linkFactory);

		return cycle;
	}

}
