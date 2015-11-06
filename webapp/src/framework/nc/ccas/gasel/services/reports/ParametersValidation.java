package nc.ccas.gasel.services.reports;

import java.util.Map;

public interface ParametersValidation {
	
	public void validate(Map<String, Object> parameters) throws IllegalArgumentException;

}
