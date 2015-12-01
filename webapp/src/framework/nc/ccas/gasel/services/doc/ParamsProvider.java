package nc.ccas.gasel.services.doc;

import java.util.Map;
import java.util.Set;

import org.apache.cayenne.Persistent;

public abstract class ParamsProvider<T> {

	private final Class<T> clazz;

	protected abstract Set<String> getProvidedParams();

	protected abstract Map<String, String> toParamsImpl(T source);

	public ParamsProvider(Class<T> clazz) {
		this.clazz = clazz;
	}

	public Map<String, String> toParams(Persistent source) {
		Map<String, String> params = toParamsImpl(clazz.cast(source));
		for (String var : getProvidedParams()) {
			if (!params.containsKey(var))
				throw new RuntimeException("Undefined variable: " + var);
		}
		return params;
	}

}
