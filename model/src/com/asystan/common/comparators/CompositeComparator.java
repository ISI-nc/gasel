package com.asystan.common.comparators;

import java.util.Comparator;

public class CompositeComparator<T> implements Comparator<T> {

	private Comparator<? super T>[] comparators;

	@SuppressWarnings("unchecked")
	public CompositeComparator(Comparator<? super T>... comparators) {
		this.comparators = comparators;
	}

	@Override
	public int compare(T o1, T o2) {
		for (Comparator<? super T> comparator : comparators) {
			int cmp = comparator.compare(o1, o2);
			if (cmp != 0) {
				return cmp;
			}
		}
		return 0;
	}

}
