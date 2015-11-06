package nc.ccas.gasel;

import nc.ccas.gasel.workflow.WorkflowEntry;

import org.apache.cayenne.DataObject;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;

public abstract class ObjectPage<T extends DataObject> extends
		ObjectCallbackPage<T> {

	@Persist("workflow")
	public abstract T getObject();

	public abstract void setObject(T object);

	protected boolean dontOpenNewEntry = false;

	/*
	 * Actions
	 */

	public void success() {
		success(getObject());
	}

	public void open(T object) {
		//prepareOpen(object);
		//redirect();
		open(object, null);
	}

	public void open(T object, ObjectCallback<?, ? super T> callback) {
		prepareOpen(object, callback);
		redirect();
	}

	protected void prepareOpen(T object) {
		prepareOpen(object, null);
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		dontOpenNewEntry = false;
	}

	protected void prepareOpen(T object, ObjectCallback<?, ? super T> callback) {
		WorkflowEntry we;
		if (dontOpenNewEntry) {
			we = getWorkflow().getCurrentEntry();
		} else {
			we = getWorkflow().getCurrentEntry()
					.open(getPageName(), getTitre());
			getWorkflow().setCurrentEntry(we);
		}
		setObject(ensureDataContext(object));
		setSuccessCallback(callback);
	}

	/* Utiles */

	public String objectStatus() {
		return objectStatus(getObject());
	}

	@Override
	public <E extends DataObject> E ensureDataContext(E obj) {
		if (getObject() == null) {
			return super.ensureDataContext(obj);
		}
		return Utils.ensureDataContext(obj, getObject().getObjectContext());
	}

}
