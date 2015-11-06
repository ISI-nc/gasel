package nc.ccas.gasel.jwcs.core.show;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.tapestry.BaseComponent;

public abstract class Id extends BaseComponent {

	public abstract DataObject getObject();

	public int getObjectId() {
		return DataObjectUtils.intPKForObject(getObject());
	}

	public boolean getHasObjectId() {
		return !getObject().getObjectId().isTemporary();
	}

}
