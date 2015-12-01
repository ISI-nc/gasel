package nc.ccas.gasel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.cayenne.DataObject;

public class BasePageLists {

	public String join(String join, Collection<?> objects) {
		StringBuffer buf = new StringBuffer();
		for (Object o : objects) {
			if (buf.length() > 0) {
				buf.append(", ");
			}
			buf.append(String.valueOf(o));
		}
		return buf.toString();
	}

	public String join(String join, Collection<? extends DataObject> objects,
			String property) {
		StringBuffer buf = new StringBuffer();
		for (DataObject o : objects) {
			if (buf.length() > 0) {
				buf.append(", ");
			}
			buf.append(String.valueOf(o.readProperty(property)));
		}
		return buf.toString();
	}

	public <T> List<T> filter(Collection<? extends T> source, Check<T> check) {
		ArrayList<T> retval = new ArrayList<T>(source.size());
		for (T t : source) {
			if (!check.check(t))
				continue;
			retval.add(t);
		}
		retval.trimToSize();
		return retval;
	}

}
