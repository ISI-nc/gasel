package nc.ccas.gasel.utils;

import java.util.Map;

public class QuickMap<K, V> {

	private final Map<K, V> map;

	public QuickMap(Map<K, V> map) {
		this.map = map;
	}

	public QuickMap<K, V> put(K key, V value) {
		map.put(key, value);
		return this;
	}

	public QuickMap<K, V> ref(K key, K referencedKey) {
		map.put(key, map.get(referencedKey));
		return this;
	}

	public Map<K, V> map() {
		return map;
	}

	public void merge(Map<? extends K, ? extends V> otherMap) {
		for (Map.Entry<? extends K, ? extends V> entry : otherMap.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public void merge(QuickMap<? extends K, ? extends V> otherMap) {
		merge(otherMap.map());
	}

}
