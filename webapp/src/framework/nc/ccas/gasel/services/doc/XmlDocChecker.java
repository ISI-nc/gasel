package nc.ccas.gasel.services.doc;

import static java.util.regex.Pattern.compile;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlDocChecker implements DocumentChecker {

	public static final XmlDocChecker INSTANCE = new XmlDocChecker();

	static final Pattern VAR_PATTERN = compile("\\$\\{([a-zA-Z0-9_ ]+)\\}");

	public DocumentCheckResult checkDocument(String documentData,
			String... allowedVariables) {
		return checkDocument(documentData, Arrays.asList(allowedVariables));
	}

	public DocumentCheckResult checkDocument(String documentData,
			Collection<String> allowedVariables) {
		return _checkDocument(documentData, //
				new TreeSet<String>(allowedVariables));
	}

	private DocumentCheckResult _checkDocument(String documentData,
			Set<String> allowedVariables) {
		DocumentCheckResult checkResult = new DocumentCheckResult();

		if (!documentData.startsWith("<?xml")) {
			checkResult.error("Format du document invalide");
			return checkResult;
		}

		Matcher matcher = VAR_PATTERN.matcher(documentData);
		while (matcher.find()) {
			String var = matcher.group(1).trim().toLowerCase();

			checkResult.variable(var);

			if (!allowedVariables.contains(var))
				checkResult.error("Variable invalide : " + var);
		}

		Set<String> variables = checkResult.variables();
		for (String var : allowedVariables) {
			if (variables.contains(var))
				continue;
			checkResult.warning("Variable inutilisée : " + var);
		}

		return checkResult;
	}

}
