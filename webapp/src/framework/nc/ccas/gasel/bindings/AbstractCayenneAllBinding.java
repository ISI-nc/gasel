package nc.ccas.gasel.bindings;

import static nc.ccas.gasel.bindings.CayenneAllBinding.query;

import java.util.Set;

import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.DataObject;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.binding.AbstractBinding;
import org.apache.tapestry.coerce.ValueConverter;

public class AbstractCayenneAllBinding extends AbstractBinding {

	private final String entity;

	protected AbstractCayenneAllBinding(String entity, String description,
			ValueConverter valueConverter, Location location) {
		super(description, valueConverter, location);
		this.entity = entity;

		if (CayenneUtils.entityResolver().getObjEntity(entity) == null) {
			throw new IllegalArgumentException("Entity not found: " + entity);
		}
	}

	public Object getObject() {
		return getValues();
	}

	protected Set<DataObject> getValues() {
		try {
			return query(DataContext.getThreadDataContext(), entity);
		} catch (Exception e) {
			throw new ApplicationRuntimeException(
					"Erreur lors d'une requète «cay-all:" + entity + "»",
					getLocation(), e);
		}
	}

}
