package nc.ccas.gasel.modelUtils;

import java.io.Serializable;

import org.apache.cayenne.DataObject;

public class FakeToOne<T extends DataObject> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8669914747636178463L;

	private final DataObject source;

	private final String sourceProperty;

	private final Class<T> destClass;

	private final String destProperty;

	private T value;

	public FakeToOne(DataObject source, String sourceProperty,
			Class<T> destClass, String destProperty) {
		this.source = source;
		this.sourceProperty = sourceProperty;
		this.destClass = destClass;
		this.destProperty = destProperty;
	}

	public T get() {
		if (value == null) {
			value = CommonQueries.fakeToOneGet(source, sourceProperty,
					destClass, destProperty);
		}

		return value;
	}

	public void set(T value) {
		this.value = value;
		CommonQueries.fakeToOneSet(source, sourceProperty, value, destProperty);
	}

}
