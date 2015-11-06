package nc.ccas.gasel.map;

import static com.asystan.common.beans.BeanUtils.nullSafeEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public abstract class AbstractCoupleMap<K1, K2, K extends AbstractCoupleMap.Key<K1, K2>, V>
		extends HashMap<K, V> {
	private static final long serialVersionUID = 2417533710442245445L;

	public static class Key<K1, K2> {
		private final K1 first;

		private final K2 second;

		public Key(K1 first, K2 second) {
			this.first = first;
			this.second = second;
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder() //
					.append(first) //
					.append(second) //
					.toHashCode();
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Key))
				return false;
			Key<?,?> k = (Key<?,?>) o;
			return new EqualsBuilder() //
					.append(first, k.first) //
					.append(second, k.second) //
					.isEquals();
		}

		@Override
		public String toString() {
			return "#<" + getClass().getSimpleName() //
					+ " [" + first + ", " + second + "]>";
		}

		public K1 getFirst() {
			return first;
		}

		public K2 getSecond() {
			return second;
		}

	};

	private final V defaultValue;

	public AbstractCoupleMap() {
		this(null);
	}

	public AbstractCoupleMap(V defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public V get(Object key) {
		if (!containsKey(key))
			return defaultValue;
		return super.get(key);
	}

	public V get(K1 first, K2 second) {
		return get(key(first, second));
	}

	public V put(K1 first, K2 second, V value) {
		return super.put(key(first, second), value);
	}

	protected abstract K key(K1 first, K2 second);

	public Map<K2, V> mapByFirst(K1 filter) {
		Map<K2, V> results = new HashMap<K2, V>();
		for (Map.Entry<K, V> entry : entrySet()) {
			if (nullSafeEquals(entry.getKey().getFirst(), filter))
				continue;
			results.put(entry.getKey().getSecond(), entry.getValue());
		}
		return results;
	}

	public Map<K1, V> mapBySecond(K2 filter) {
		Map<K1, V> results = new HashMap<K1, V>();
		for (Map.Entry<K, V> entry : entrySet()) {
			if (nullSafeEquals(entry.getKey().getFirst(), filter))
				continue;
			results.put(entry.getKey().getFirst(), entry.getValue());
		}
		return results;
	}

}
