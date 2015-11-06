package nc.ccas.gasel.model.core.docs.valid;

import java.util.Arrays;
import java.util.Collection;

import nc.ccas.gasel.model.core.docs.ModeleDocument;
import nc.ccas.gasel.services.doc.DocumentCheckResult;
import nc.ccas.gasel.services.doc.XmlDocChecker;

import org.apache.cayenne.PersistenceState;

public class ModelDocUtils {

	/**
	 * Same as check, but only if the document is new and not null.
	 * 
	 * Use this one unless you're unsure of the document in the database.
	 */
	public static DocumentCheckResult fastCheck(ModeleDocument doc,
			String... allowedVariables) {
		return fastCheck(doc, Arrays.asList(allowedVariables));
	}

	/**
	 * Same as check, but only if the document is new and not null.
	 * 
	 * Use this one unless you're unsure of the document in the database.
	 */
	public static DocumentCheckResult fastCheck(ModeleDocument doc,
			Collection<String> allowedVariables) {
		if (doc == null || doc.getPersistenceState() != PersistenceState.NEW)
			return new DocumentCheckResult();

		return check(doc, allowedVariables);
	}

	/**
	 * Check the document.
	 */
	public static DocumentCheckResult check(ModeleDocument doc,
			String... allowedVariables) {
		return check(doc, Arrays.asList(allowedVariables));
	}

	/**
	 * Check the document.
	 */
	public static DocumentCheckResult check(ModeleDocument doc,
			Collection<String> allowedVariables) {
		return XmlDocChecker.INSTANCE //
				.checkDocument(doc.getData(), allowedVariables);
	}

}
