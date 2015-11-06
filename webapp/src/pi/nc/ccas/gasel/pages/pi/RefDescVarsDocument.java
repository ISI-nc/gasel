package nc.ccas.gasel.pages.pi;

import java.io.Serializable;
import java.util.Collection;

public class RefDescVarsDocument implements Serializable {
	private static final long serialVersionUID = -6705951635114396947L;

	private final String key;

	private final String description;

	private final Collection<String> vars;

	public RefDescVarsDocument(String key, String description,
			Collection<String> vars) {
		this.key = key;
		this.description = description;
		this.vars = vars;
	}

	public String getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}

	public Collection<String> getVars() {
		return vars;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((description == null) ? 0 : description.hashCode());
		result = PRIME * result + ((key == null) ? 0 : key.hashCode());
		result = PRIME * result + ((vars == null) ? 0 : vars.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RefDescVarsDocument other = (RefDescVarsDocument) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (vars == null) {
			if (other.vars != null)
				return false;
		} else if (!vars.equals(other.vars))
			return false;
		return true;
	}

}
