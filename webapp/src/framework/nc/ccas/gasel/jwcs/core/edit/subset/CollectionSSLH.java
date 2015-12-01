package nc.ccas.gasel.jwcs.core.edit.subset;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * Simple wrapper for a collection.
 * 
 * @author ISI.NC - Mikaël Cluseau
 * 
 * @param <T>
 *            Collection's element type.
 */
public class CollectionSSLH<T> implements SubSetListHandler<T>, Serializable {

	private final Collection<T> list;

	public CollectionSSLH(Collection<T> list) {
		this.list = list;
	}

	public boolean contains(T value) {
		return list.contains(value);
	}

	public void add(T value) {
		list.add(value);
	}

	public void remove(T value) {
		list.remove(value);
	}

	public Iterator<T> iterator() {
		return list.iterator();
	}

}
