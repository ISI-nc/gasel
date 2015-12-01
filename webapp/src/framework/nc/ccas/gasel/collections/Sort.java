package nc.ccas.gasel.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.cayenne.query.Ordering;
import org.apache.commons.collections.comparators.ComparableComparator;

public class Sort<T> implements Comparator<T>, Iterable<T> {

	private final Collection<? extends T> values;

	private final List<Comparator<? super T>> comparatorChain = new LinkedList<Comparator<? super T>>();

	public Sort() {
		values = null;
	}

	public Sort(Collection<? extends T> values) {
		this.values = values;
	}

	public Sort<T> byToString() {
		return by(ToStringComparator.INSTANCE);
	}

	public Sort<T> getByToString() {
		return byToString();
	}

	public Sort<T> by(String property) {
		return by(property, "asc");
	}

	public Sort<T> by(String property, String order) {
		boolean asc = order.toLowerCase().equals("asc");
		return by(new Ordering(property, asc ? true : false));
	}

	public Sort<T> by(Comparator<? super T> comp) {
		comparatorChain.add(comp);
		return this;
	}

	public int compare(T o1, T o2) {
		for (Comparator<? super T> comparator : comparatorChain) {
			int value = comparator.compare(o1, o2);
			if (value != 0) {
				return value;
			}
		}
		return 0;
	}

	public List<T> results() {
		return sort(values);
	}

	public List<T> getResults() {
		return results();
	}

	public List<T> sort(Collection<? extends T> inputList) {
		List<T> values = new ArrayList<T>();
		values.addAll(inputList);
		Collections.sort(values, this);
		return values;
	}

	@SuppressWarnings("unchecked")
	public Sort<T> comparable() {
		comparatorChain.add(ComparableComparator.getInstance());
		return this;
	}

	public Iterator<T> iterator() {
		return results().iterator();
	}

}
