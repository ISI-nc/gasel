package nc.ccas.gasel.services.doc;

import static nc.ccas.gasel.services.doc.XmlDocChecker.VAR_PATTERN;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;

public class XmlDocChanger {

	private final List<DocumentPart> document = new LinkedList<DocumentPart>();

	private final Set<String> expectedVariables = new TreeSet<String>();

	private final Map<String, String> substitutions = new HashMap<String, String>();

	public XmlDocChanger(String documentData) {
		parse(documentData);
	}

	/**
	 * 
	 * @param varName
	 * @param value
	 * @return
	 * @throws IllegalArgumentException
	 *             if there's already a substitution for <code>varName</code>.
	 */
	public XmlDocChanger subst(Map<String, String> substs) {
		for (Map.Entry<String, String> entry : substs.entrySet()) {
			subst(entry.getKey(), entry.getValue());
		}
		return this;
	}

	/**
	 * 
	 * @param varName
	 * @param value
	 * @return
	 * @throws IllegalArgumentException
	 *             if there's already a substitution for <code>varName</code>.
	 */
	public XmlDocChanger subst(String varName, String value) {
		if (substitutions.containsKey(varName))
			throw new IllegalArgumentException("Duplicate substitution: "
					+ varName);
		substitutions.put(varName, value);
		return this;
	}

	/**
	 * TODO JavaDoc
	 * 
	 * @return
	 */
	public String result() {
		StringWriter writer = new StringWriter();
		try {
			writeTo(writer);
		} catch (IOException e) {
			// Should never happen.
			throw new RuntimeException(e);
		}
		return writer.toString();
	}

	/**
	 * TODO JavaDoc
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void writeTo(Writer writer) throws IOException {
		for (DocumentPart part : document) {
			part.write(writer);
		}
	}

	public void writeTo(OutputStream stream) throws IOException {
		writeTo(new OutputStreamWriter(stream));
	}

	private void parse(String documentData) {
		Matcher matcher = VAR_PATTERN.matcher(documentData);
		int start = 0;
		while (matcher.find()) {
			if (matcher.start() > start) {
				String textBeforeMatch = documentData //
						.substring(start, matcher.start());
				document.add(new LiteralPart(textBeforeMatch));
			}
			String variableName = matcher.group(1);
			expectedVariables.add(variableName);
			document.add(new VariablePart(variableName));
			start = matcher.end();
		}
		if (start < documentData.length()) {
			String textToEnd = documentData.substring(start);
			document.add(new LiteralPart(textToEnd));
		}
	}

	// ------------------------------------------------------------------
	// Morceaux de document
	//

	private interface DocumentPart {
		void write(Writer out) throws IOException;
	}

	private class LiteralPart implements DocumentPart {
		private final String value;

		public LiteralPart(String value) {
			this.value = value;
		}

		public void write(Writer out) throws IOException {
			out.append(value);
		}
	}

	private class VariablePart implements DocumentPart {
		private final String variableName;

		public VariablePart(String variableName) {
			this.variableName = variableName.replaceAll("\\s+", " ").trim();
		}

		public void write(Writer out) throws IOException {
			String value = substitutions.get(variableName);
			if (value == null)
				throw new RuntimeException("No substitution for "
						+ variableName);
			out.append(value);
		}
	}

}
