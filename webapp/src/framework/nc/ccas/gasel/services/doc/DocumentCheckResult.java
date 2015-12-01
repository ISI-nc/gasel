package nc.ccas.gasel.services.doc;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import nc.ccas.gasel.CheckResult;

public class DocumentCheckResult implements CheckResult {

	private List<String> errors = new LinkedList<String>();

	private List<String> warnings = new LinkedList<String>();

	private Set<String> variables = new TreeSet<String>();

	public void error(String error) {
		errors.add(error);
	}

	public void warning(String warning) {
		warnings.add(warning);
	}

	public void variable(String variable) {
		variables.add(variable);
	}

	public List<String> errors() {
		return Collections.unmodifiableList(errors);
	}

	public List<String> warnings() {
		return Collections.unmodifiableList(warnings);
	}

	public Set<String> variables() {
		return Collections.unmodifiableSet(variables);
	}

	public List<String> getErrors() {
		return errors();
	}

	public List<String> getWarnings() {
		return warnings();
	}

}
