package nc.ccas.gasel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.collections.ToStringComparator;

import org.apache.cayenne.query.Ordering;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.hivemind.util.Defense;

public class BasePageSort<T> implements Comparator<T>, Iterable<T> {

	private final Collection<? extends T> values;

	private final List<Comparator<? super T>> comparatorChain = new LinkedList<Comparator<? super T>>();

	public BasePageSort() {
		values = null;
	}

	public BasePageSort(Collection<? extends T> values) {
		this.values = values;
	}

	public BasePageSort<T> byToString() {
		return by(ToStringComparator.INSTANCE);
	}

	public BasePageSort<T> getByToString() {
		return byToString();
	}

	public BasePageSort<T> by(String property) {
		return by(property, "asc");
	}

	public BasePageSort<T> by(String property, String order) {
		Defense.notNull(property, "property");
		
		boolean asc = order.toLowerCase().equals("asc");
		return by(new Ordering(property, asc ? true : false));
	}

	public BasePageSort<T> by(Comparator<? super T> comp) {
		Defense.notNull(comp, "comparator");
		
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
		Defense.notNull(values, "values");
		return sort(values);
	}

	public List<T> getResults() {
		return results();
	}

	public List<T> sort(Collection<? extends T> inputList) {
		Defense.notNull(inputList, "inputList");
		List<T> values = new ArrayList<T>(inputList.size());
		values.addAll(inputList);
		Collections.sort(values, this);
		return values;
	}

	@SuppressWarnings("unchecked")
	public BasePageSort<T> comparable() {
		comparatorChain.add(ComparableComparator.getInstance());
		return this;
	}

	public Iterator<T> iterator() {
		return results().iterator();
	}

}
