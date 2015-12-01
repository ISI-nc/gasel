package nc.ccas.gasel.stats.repr;

import java.util.Map;

import nc.ccas.gasel.stats.Representation;

public class TranslateRepr extends Representation<Object> {

	private final Map<?, String> map;

	public TranslateRepr(Map<?, String> map) {
		super(Object.class);
		assert map != null;
		this.map = map;
	}

	@Override
	public String representationImpl(Object obj) {
		if (!map.containsKey(obj)) {
			throw new RuntimeException("Can't translate " + obj + " ("
					+ obj.getClass() + ")");
		}
		return map.get(obj);
	}

}