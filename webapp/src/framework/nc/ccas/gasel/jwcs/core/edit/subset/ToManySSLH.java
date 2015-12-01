package nc.ccas.gasel.jwcs.core.edit.subset;

import java.util.Iterator;
import java.util.List;

import org.apache.cayenne.DataObject;

public class ToManySSLH<T extends DataObject> implements SubSetListHandler<T> {

	private final DataObject source;

	private final String listName;

	public ToManySSLH(DataObject source, String listName) {
		this.source = source;
		this.listName = listName;
	}

	public boolean contains(T value) {
		return list().contains(value);
	}

	public void add(T value) {
		source.addToManyTarget(listName, value, true);
	}

	public void remove(T value) {
		source.removeToManyTarget(listName, value, true);
	}

	public Iterator<T> iterator() {
		return list().iterator();
	}

	@SuppressWarnings("unchecked")
	private List<T> list() {
		return (List<T>) source.readProperty(listName);
	}

}
