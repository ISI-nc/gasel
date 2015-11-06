package nc.ccas.gasel;

import static org.apache.commons.lang.StringUtils.join;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Smart wrapper around a Map<Object[], Object>.
 * 
 * @author ISI.NC - Mikaël Cluseau
 * 
 */
public class CompositeHashMap implements Serializable {
	private static final long serialVersionUID = 3238203282241686004L;

	private final Map<Key, Object> data = new HashMap<Key, Object>();

	// ------------------------------------------------------------------

	public boolean contains(Object... key) {
		return data.containsKey(new Key(key));
	}

	// ------------------------------------------------------------------

	public Object get(Object... key) {
		return data.get(new Key(key));
	}

	/**
	 * Renvoie la valeur stockée sous la clé <code>key</code>, ou la crée en
	 * utilisant <code>factory</code>.
	 * <p>
	 * Remarque : on affaibli les vérifications sur le type retourné. La
	 * proximité avec la factory lors d'un appel est considérée suffisante.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(@SuppressWarnings("rawtypes") ValueFactory factory, Object... key) {
		if (!contains(key)) {
			Object value = factory.buildValue();
			put(key, value);
			return (T) value;
		} else {
			return (T) get(key);
		}
	}

	// ------------------------------------------------------------------

	public void put(Object... keyAndValue) {
		if (keyAndValue.length == 0) {
			put(new Object[] {}, null);
			return;
		}

		Object[] key = new Object[keyAndValue.length - 1];
		System.arraycopy(keyAndValue, 0, key, 0, keyAndValue.length - 1);
		Object value = keyAndValue[keyAndValue.length - 1];

		put(key, value);
	}

	public void put(Object[] key, Object value) {
		data.put(new Key(key), value);
	}

	// ------------------------------------------------------------------

	public void clear() {
		data.clear();
	}

	// ------------------------------------------------------------------
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(super.toString()).append(": ");
		buf.append(data);
		return buf.toString();
	}

	// ------------------------------------------------------------------

	private class Key implements Serializable {
		private static final long serialVersionUID = -4088653726160219762L;

		private final Object[] key;

		public Key(Object[] key) {
			this.key = key;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Key))
				return false;
			Key o = (Key) obj;
			if (key == o.key) {
				return true;
			}
			if (key == null || o.key == null) {
				return false;
			}
			if (key.length != o.key.length) {
				return false;
			}
			EqualsBuilder eqb = new EqualsBuilder();
			for (int i = 0; i < key.length; i++) {
				eqb.append(key[i], o.key[i]);
			}
			return eqb.isEquals();
		}

		@Override
		public int hashCode() {
			HashCodeBuilder hcb = new HashCodeBuilder();
			for (Object keyPart : key) {
				hcb.append(keyPart);
			}
			return hcb.toHashCode();
		}
		
		@Override
		public String toString() {
			return "Key[" + join(key, ", ") + "]";
		}

	}

}
