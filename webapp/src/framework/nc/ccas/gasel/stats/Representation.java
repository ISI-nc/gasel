package nc.ccas.gasel.stats;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public abstract class Representation<T> {

	public static final String NULL_REPR = "--";

	public abstract String representationImpl(T obj);

	private final Class<T> clazz;

	public Representation(Class<T> clazz) {
		this.clazz = clazz;
	}

	public final Set<String> representation(List<Object> o) {
		while (o.remove(null))
			;
		if (o.isEmpty())
			return Collections.singleton(NULL_REPR);

		Set<String> values = new TreeSet<String>();

		for (Object o1 : ((Iterable<?>) o)) {
			if (o1 == null)
				continue;
			if (!clazz.isInstance(o1))
				throw new ClassCastException(o1.getClass() + " to " + clazz);
			values.add(representationImpl(clazz.cast(o1)));
		}

		return values;
	}

}
