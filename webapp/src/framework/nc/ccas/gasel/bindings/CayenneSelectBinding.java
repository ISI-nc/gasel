package nc.ccas.gasel.bindings;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.binding.AbstractBinding;
import org.apache.tapestry.coerce.ValueConverter;

public class CayenneSelectBinding extends AbstractBinding {

	private final SelectQuery query;

	protected CayenneSelectBinding(SelectQuery query, String description,
			ValueConverter valueConverter, Location location) {
		super(description, valueConverter, location);
		this.query = query;
	}

	public Object getObject() {
		try {
			return DataContext.getThreadDataContext().performQuery(query);
		} catch (Exception e) {
			throw new ApplicationRuntimeException(
					"Erreur lors d'une requète: root=" + query.getRoot()
							+ "; qualifier=" + query.getQualifier(),
					getLocation(), e);
		}
	}

}
