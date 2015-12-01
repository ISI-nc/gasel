package nc.ccas.gasel;

import nc.ccas.gasel.workflow.WorkflowEntry;

import org.apache.cayenne.access.DataContext;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.annotations.Persist;

public abstract class ObjectCallbackPage<T> extends BasePage {

	/**
	 * Callback utilisé lors de l'enregistrement avec succès de l'objet édité.
	 * Il est appelé dans le contexte du parent.
	 */
	@Persist("workflow")
	public abstract ObjectCallback<?, ? super T> getSuccessCallback();

	public abstract void setSuccessCallback(ObjectCallback<?, ? super T> value);

	/*
	 * Actions
	 */

	public void success(T object) {
		ObjectCallback<?, ? super T> successCallback = getSuccessCallback();
		WorkflowEntry myEntry = getWorkflow().getCurrentEntry();
		WorkflowEntry parent = myEntry.getParent();
		// Activation du parent
		getWorkflow().setCurrentEntry(parent);
		// Le contexte par défaut devient celui du parent
		DataContext.bindThreadDataContext(parent.getObjectContext());
		if (successCallback != null) {
			try {
				successCallback.performCallback(getRequestCycle(), object);
			} catch (RedirectException re) {
				throw re;
			} catch (PageRedirectException pre) {
				throw pre;
			} catch (Exception e) {
				// En cas de problème on restaure l'entrée courante
				getWorkflow().setCurrentEntry(myEntry);
				throw new RuntimeException(e);
			}
		}
		getWorkflow().close(myEntry, getRequestCycle());
		getWorkflow().redirect(getRequestCycle());
	}

	/*
	 * Utils
	 */

	public void activate(BasePage page, String callbackMethodName) {
		activate(new MethodObjectCallback(page, callbackMethodName));
	}

	public void activate(ObjectCallback<?, ? super T> callback) {
		prepareActivation(callback);
		redirect(getRequestCycle());
	}

	protected void prepareActivation(ObjectCallback<?, ? super T> callback) {
		prepareActivation();
		setSuccessCallback(callback);
	}

}
