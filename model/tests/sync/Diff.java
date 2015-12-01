package sync;

import java.util.Set;
import java.util.TreeSet;

public class Diff {

	public final Set<String> missing = new TreeSet<String>();

	public final Set<String> removable = new TreeSet<String>();

	public final Set<String> intersection = new TreeSet<String>();

	public Diff(Set<String> from, Set<String> to) {
		for (String elt : to) {
			if (from.contains(elt)) {
				intersection.add(elt);
			} else {
				missing.add(elt);
			}
		}
		for (String elt : from) {
			if (!to.contains(elt)) {
				removable.add(elt);
			}
		}
	}

}
