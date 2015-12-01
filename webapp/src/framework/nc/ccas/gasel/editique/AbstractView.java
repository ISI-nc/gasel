package nc.ccas.gasel.editique;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public abstract class AbstractView<T> implements View {

	protected abstract void writeImpl(PrintWriter out, T object)
			throws IOException;

	public abstract String getStarpageFile();

	private final Class<T> clazz;

	public AbstractView(Class<T> clazz) {
		this.clazz = clazz;
	}

	public final void write(Writer out, Object object) {
		try {
			writeImpl(new PrintWriter(out), clazz.cast(object));
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}