package com.asystan.common.lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.asystan.common.Filter;

public class ListUtils {

	public static <T> List<T> filter(Collection<T> values,
			Filter<? super T> filter) {
		ArrayList<T> list = new ArrayList<T>(values.size());
		for (T value : values) {
			if (filter.accept(value)) {
				list.add(value);
			}
		}
		list.trimToSize();
		return list;
	}

	public static <T> List<T> cast(Class<T> clazz, Collection<?> source) {
		ArrayList<T> list = new ArrayList<T>(source.size());
		for (Object o : source) {
			list.add(clazz.cast(o));
		}
		return list;
	}

}
