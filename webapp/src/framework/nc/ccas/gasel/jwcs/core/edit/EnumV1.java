package nc.ccas.gasel.jwcs.core.edit;

import nc.ccas.gasel.psm.EnumV1PSM;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry.form.IPropertySelectionModel;

public abstract class EnumV1 extends Enum {

	@Override
	public IPropertySelectionModel getModel() {
		ObjectContext dc = getObjectContext();
		SelectQuery query = new SelectQuery(getEnum());
		query.addOrdering("enumeration.label", true);
		return new EnumV1PSM(dc.performQuery(query), getObjectContext(),
				objClass(dc));
	}

	private Class<? extends DataObject> objClass(ObjectContext dc) {
		Class<? extends DataObject> objClass = dc.getEntityResolver()
				.getObjEntity(getEnum()).getJavaClass()
				.asSubclass(DataObject.class);
		return objClass;
	}

}
