package nc.ccas.gasel.services.fop;

import java.util.HashMap;
import java.util.Map;

import nc.ccas.gasel.starjet.aides.ArreteFo;
import nc.ccas.gasel.starjet.aides.CourrierAideGaz;

public class XslFoOutputs {

	private static Map<String, XslFoOutput<?>> OUTPUTS = new HashMap<String, XslFoOutput<?>>();

	public static XslFoOutput<?> get(String view) {
		return OUTPUTS.get(view);
	}

	public static void register(String view, XslFoOutput<?> output) {
		if (OUTPUTS.containsKey(view)) {
			throw new RuntimeException("Duplicate: " + view);
		}
		OUTPUTS.put(view, output);
	}

	static {
		register("arrete", new ArreteFo());
		register("aide.courrier.gaz", new CourrierAideGaz());
	}

}
