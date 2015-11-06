package nc.ccas.gasel;

import java.util.Collection;

public interface CheckResult {

	public Collection<String> getErrors();

	public Collection<String> getWarnings();

}
