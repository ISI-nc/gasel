package com.asystan.common.cache_new;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Réimplémentation minimale pour suppression d'une dépendance morte.
 */
public abstract class CacheMap<K, V> implements Serializable {
	private static final long serialVersionUID = -2224074434691993797L;
	
	public static final int TREEMAP = 1;
	public static final int HASHMAP = 2;

	private static final Object NULL = new Object();

	private final Map<K, SoftReference<Object>> cache;

	protected abstract V buildValue(K key);

	public CacheMap() {
		this(HASHMAP);
	}

	public CacheMap(int type) {
		switch (type) {
		case TREEMAP:
			cache = new TreeMap<K, SoftReference<Object>>();
			break;

		case HASHMAP:
			cache = new HashMap<K, SoftReference<Object>>();
			break;

		default:
			throw new RuntimeException("Invalid type: " + type);
		}
	}

	public V getValue(K key) {
		synchronized (this) {
			if (!cache.containsKey(key)) {
				V value = buildValue(key);
				putValue(key, value);
				return value;
			}
			SoftReference<Object> ref = cache.get(key);
			if (ref.get() == null) {
				V value = buildValue(key);
				putValue(key, value);
				return value;
			} else {
				return unwrap(ref);
			}
		}
	}

	public void clear() {
		synchronized (this) {
			cache.clear();
		}
	}

	@SuppressWarnings("unchecked")
	private V unwrap(SoftReference<Object> ref) {
		Object value = ref.get();
		if (value == NULL) {
			return null;
		}
		return (V) value;
	}

	private void putValue(K key, V value) {
		cache.put(key, new SoftReference<Object>(value == null ? NULL : value));
	}

}