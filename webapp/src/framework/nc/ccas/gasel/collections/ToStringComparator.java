package nc.ccas.gasel.collections;

import java.util.Comparator;

public class ToStringComparator implements Comparator<Object> {

	public static final Comparator<Object> INSTANCE = new ToStringComparator();

	public int compare(Object o1, Object o2) {
		return o1.toString().compareTo(o2.toString());
	}

}
