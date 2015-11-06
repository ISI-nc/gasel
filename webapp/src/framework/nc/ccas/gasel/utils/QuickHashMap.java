package nc.ccas.gasel.utils;

import java.util.HashMap;

public class QuickHashMap<K, V> extends QuickMap<K, V> {

	public QuickHashMap() {
		super(new HashMap<K, V>());
	}

}
