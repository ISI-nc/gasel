package nc.ccas.gasel.services.fop;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.markup.MarkupWriterImpl;
import org.apache.tapestry.markup.UTFMarkupFilter;

public abstract class XslFoOutput<T> {

	protected abstract void writeXslFoImpl(T source, IMarkupWriter writer);

	private final Class<T> clazz;

	public XslFoOutput(Class<T> clazz) {
		this.clazz = clazz;
	}

	public void writeXslFo(Object source, IMarkupWriter writer) {
		writeXslFoImpl(clazz.cast(source), writer);
	}

	public String writeXslFo(Object source) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		IMarkupWriter writer = new MarkupWriterImpl( //
				"text/xml", printWriter, new UTFMarkupFilter());

		writeXslFo(source, writer);

		printWriter.close();
		return stringWriter.toString();
	}

}
