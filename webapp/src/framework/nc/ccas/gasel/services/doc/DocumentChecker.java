package nc.ccas.gasel.services.doc;

import java.util.Collection;

public interface DocumentChecker {

	/**
	 * Checks a document and returns a check result.
	 */
	public DocumentCheckResult checkDocument(String documentData,
			String... allowedVariables);

	/**
	 * Checks a document and returns a check result.
	 */
	public DocumentCheckResult checkDocument(String documentData,
			Collection<String> allowedVariables);

}
