package nc.ccas.gasel.services.reports;

import java.lang.reflect.InvocationTargetException;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperReportsContext;

import org.apache.tapestry.util.ContentType;

public class ReportSupport {

	private final ContentType contentType;
	private final Class<? extends JRExporter> exporterClass;

	public ReportSupport(ContentType contentType,
			Class<? extends JRExporter> exporterClass) {
		this.contentType = contentType;
		this.exporterClass = exporterClass;
	}

	public ReportSupport(String contentType, Class<? extends JRExporter> exporterClass) {
		this(new ContentType(contentType), exporterClass);
	}

	public ContentType getContentType() {
		return contentType;
	}

	public JRExporter buildExporter() {
		return buildExporter(DefaultJasperReportsContext.getInstance());
	}

	public JRExporter buildExporter(DefaultJasperReportsContext context) {
		try {
			return exporterClass.getConstructor(JasperReportsContext.class)
					.newInstance(context);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

}
