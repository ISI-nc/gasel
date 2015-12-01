package nc.ccas.gasel.services.reports;

import static java.util.Collections.singletonMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import nc.ccas.gasel.services.DataTransformation;
import nc.ccas.gasel.services.OutputService;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.conf.Configuration;
import org.apache.log4j.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebContext;

public class ReportService extends OutputService {
	
	private static final Logger LOG = Logger.getLogger(ReportService.class);

	public static final String NAME = "reports";

	public static final String FORMAT_PARAM = "format";

	private static Map<String, DataTransformation> transformations = new TreeMap<>();
	private static Map<String, ParametersValidation> validations = new TreeMap<>();

	private static Map<String, ReportSupport> formats = new TreeMap<>();

	public static void invoke(IRequestCycle cycle, String view, String key,
			Object value) {
		invoke(cycle, NAME, view, singletonMap(key, value));
	}

	public static void invoke(IRequestCycle cycle, String view, String key,
			Object value, String format) {
		invokeWithParameters(cycle, view, format, singletonMap(key, value));
	}
	
	public static void invokeWithParameters(IRequestCycle cycle, String view, String format, Map<String, Object> reportParameters) {
		if (!formats.containsKey(format)) {
			throw new IllegalArgumentException("Unsupported format: " + format);
		}
		Map<String, Object> parameters = new TreeMap<>(reportParameters);
		parameters.put(FORMAT_PARAM, format);
		invoke(cycle, NAME, view, parameters);
		
	}

	public static void invoke(IRequestCycle cycle, String view,
			Map<String, ?> data) {
		invoke(cycle, NAME, view, data);
	}

	/**
	 * 
	 * @param viewName
	 *            viewName + ":" + format. Ex: <code>edition-bons:pdf</code>.
	 * @param dataTransformation
	 *            The transformation to apply.
	 */
	public synchronized static void registerTransformation(String viewName,
			DataTransformation dataTransformation) {
		transformations.put(viewName, dataTransformation);
	}

	/**
	 * 
	 * @param viewName
	 *            viewName. Ex: <code>edition-bons</code>.
	 * @param validation
	 *            The validation to apply.
	 */
	public synchronized static void registerValidation(String viewName,
			ParametersValidation validation) {
		validations.put(viewName, validation);
	}

	public static ReportSupport getSupportForExtension(String ext) {
		return formats.get(ext);
	}

	public static Set<String> getSupportedFormats() {
		return new TreeSet<>(formats.keySet());
	}

	static {
		formats.put("pdf", new ReportSupport(PDF, JRPdfExporter.class));

		// Formats OpenDocument
		formats.put("odt", new ReportSupport(
				"application/vnd.oasis.opendocument.text", JROdtExporter.class));
		formats.put("ods", new ReportSupport(
				"application/vnd.oasis.opendocument.spreadsheet",
				JROdsExporter.class));

		// Formats ms office
		formats.put("docx", new ReportSupport("application/msword",
				JRDocxExporter.class));
		formats.put("xlsx", new ReportSupport("application/msexcel",
				JRXlsxExporter.class));
		formats.put("xls", new ReportSupport("application/msexcel",
				JRXlsExporter.class));

		// edition-bons
		registerTransformation("edition-bons:pdf", new DataTransformation() {
			@Override
			public void transformData(byte[] data, OutputStream output)
					throws IOException {
				PDDocument doc = PDDocument
						.load(new ByteArrayInputStream(data));
				for (Object o : doc.getDocumentCatalog().getAllPages()) {
					PDPage page = (PDPage) o;

					page.setRotation(180);
				}

				try {
					doc.save(output);
				} catch (COSVisitorException e) {
					throw new RuntimeException(e);
				} finally {
					doc.close();
				}
			}
		});

		registerValidation("edition-bons", new ParametersValidation() {
			@Override
			public void validate(Map<String, Object> parameters)
					throws IllegalArgumentException {
				// We must validate here since Jasper reports have no validation
				// AND $X{IN, ...} don't handle empty list correctly : when the
				// list
				// is empty, we expect "x IN () == false" but it's true, thus
				// renders
				// every single line of the table...
				//
				// Here, we just try to fallback to an intuitive behaviour :
				// render nothing!
				//
				Collection<?> bons = (Collection<?>) parameters.get("BONS_ID");
				if (bons == null || bons.isEmpty()) {
					parameters.put("BONS_ID", Collections.singleton(-1));
				}
			}
		});
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getExtension(IRequestCycle cycle, Map<String, ?> parameters) {
		if (parameters.containsKey(FORMAT_PARAM)) {
			return (String) parameters.get(FORMAT_PARAM);
		}
		return "pdf";
	}

	@Override
	public ContentType getContentType(IRequestCycle cycle,
			Map<String, ?> parameters) {
		return getReportSupport(cycle, parameters).getContentType();
	}

	private ReportSupport getReportSupport(IRequestCycle cycle,
			Map<String, ?> parameters) {
		String ext = getExtension(cycle, parameters);
		if (!formats.containsKey(ext)) {
			throw new IllegalArgumentException("No renderer for extension \""
					+ ext + "\"");
		}
		return getSupportForExtension(ext);
	}

	@Override
	public boolean doesViewExists(String viewName) {
		return true; // we don't have this info here
	}

	@Override
	protected void render(OutputStream output, IRequestCycle cycle,
			String view, Map<String, ?> parameters) throws IOException {

		String viewDir = context.getRealPath("/WEB-INF/reports");
		String viewPath = viewDir + "/" + view + ".jasper";

		ByteArrayOutputStream data = new ByteArrayOutputStream();
		InputStream viewAsStream = new FileInputStream(viewPath);

		Connection connection = null;
		try {
			// render

			Map<String, Object> params = convertMap(parameters, viewDir);
			validateParameters(view, params);

			DataNode dataNode = (DataNode) Configuration
					.getSharedConfiguration().getDomain().getDataNodes()
					.iterator().next();
			connection = dataNode.getDataSource().getConnection();
			renderReport(cycle, parameters, viewAsStream, connection, params,
					data);

			DataTransformation transformation = transformations.get(view + ":"
					+ getExtension(cycle, parameters));
			if (transformation == null) {
				transformation = DataTransformation.IDENTITY;
			}
			transformation.transformData(data.toByteArray(), output);

		} catch (JRException | SQLException e) {
			throw new RuntimeException(e);
		} finally {
			safeClose(connection);
			safeClose(viewAsStream);
		}
	}

	private void validateParameters(String view, Map<String, Object> parameters) {
		if (validations.containsKey(view)) {
			validations.get(view).validate(parameters);
		}
	}

	private void renderReport(IRequestCycle cycle, Map<String, ?> parameters,
			InputStream reportStream, Connection connection,
			Map<String, Object> reportParameters, ByteArrayOutputStream output)
			throws JRException {
		DefaultJasperReportsContext reportsContext = DefaultJasperReportsContext
				.getInstance();

		JRExporter exporter = getReportSupport(cycle, parameters)
				.buildExporter(reportsContext);

		JasperFillManager jasperFillManager = JasperFillManager
				.getInstance(reportsContext);
		LOG.info("Rendering report " + parameters);
		JasperPrint jasperPrint = jasperFillManager.fill(reportStream,
				reportParameters, connection);
		LOG.info("Rendered report " + parameters);

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
		exporter.exportReport();
	}

	private void safeClose(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void safeClose(InputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, Object> convertMap(Map<String, ?> map, String viewDir) {
		Map<String, Object> newMap = new TreeMap<>(map);
		if (!newMap.containsKey("SUBREPORT_DIR")) {
			newMap.put("SUBREPORT_DIR", viewDir + "/");
		}
		if (!newMap.containsKey(JRParameter.REPORT_LOCALE)) {
			newMap.put(JRParameter.REPORT_LOCALE, Locale.FRANCE);
		}
		return newMap;
	}

	private WebContext context;

	public void setContext(WebContext context) {
		this.context = context;
	}

}
