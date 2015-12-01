package nc.ccas.gasel.jwcs.core;

import java.io.IOException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java_gaps.ResourceUtils;
import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.model.core.docs.ModeleDocument;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.request.IUploadFile;

public abstract class Document extends BaseComponent {

	public static final String ENCODING = "UTF-8";

	@Parameter(required = true)
	public abstract ModeleDocument getDocument();

	public abstract void setDocument(ModeleDocument document);
	
	@Parameter
	public abstract Collection<String> getVariables();
	
	public abstract IUploadFile getFile();
	
	public abstract String getVariable();

	public void remove() {
		getDocument().getObjectContext().deleteObject(getDocument());
		setDocument(null);
	}

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		super.renderComponent(writer, cycle);

		if (cycle.isRewinding()) {
			try {
				updateDocument();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void updateDocument() throws IOException {
		if (getFile() == null)
			return;

		String data = ResourceUtils.readStream(getFile().getStream(), ENCODING);
		data = cleanData(data);
		ModeleDocument document = getDocument();
		if (document == null) // Seul cas possible normalement, mais on blinde
			document = newObject(ModeleDocument.class);

		document.setData(data);
		setDocument(document);
	}

	private static final Pattern VAR_PATTERN = Pattern
			.compile("\\$\\{[^\\}]+\\}");

	private static final Pattern TAG_PATTERN = Pattern.compile("<[^>]+>");

	private static String cleanData(String data) {
		StringBuilder buf = new StringBuilder(data.length());

		Matcher matcher = VAR_PATTERN.matcher(data);
		int lastEnd = 0;
		while (matcher.find()) {
			buf.append(data.substring(lastEnd, matcher.start()));

			String match = matcher.group();
			
			// Récupération des tags
			Matcher matcher2 = TAG_PATTERN.matcher(match);
			while (matcher2.find()) {
				buf.append(matcher2.group());
			}
			
			// Calcul de la chaîne nettoyée
			match = TAG_PATTERN.matcher(match).replaceAll("");
			buf.append(match);

			// Préparation de la suite
			lastEnd = matcher.end();
		}
		buf.append(data.substring(lastEnd));

		return buf.toString();
	}

}
