package nc.ccas.gasel.utils;

import java.util.TreeMap;

public class QuickTreeMap<K, V> extends QuickMap<K, V> {

	public QuickTreeMap() {
		super(new TreeMap<K, V>());
	}

}
