package nc.ccas.gasel.workflow;

import java.util.Map;

import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.engine.state.StateObjectFactory;

public class WorkflowScopeManager implements IWorkflowScopeManager {

	private ApplicationStateManager stateManager;

	private String workflowName;

	/*
	 * IWorkflowScopeManager
	 */

	public boolean exists(String objectName) {
		return workflowExists() && currentContext().containsKey(objectName);
	}

	public Object get(String objectName, StateObjectFactory factory) {
		objectName = buildKey(objectName);
		if (!currentContext().containsKey(objectName)) {
			synchronized (this) {
				if (!currentContext().containsKey(objectName)) {
					currentContext().put(objectName,
							factory.createStateObject());
				}
			}
		}
		return currentContext().get(objectName);
	}

	public void store(String objectName, Object stateObject) {
		currentContext().put(objectName, stateObject);
	}

	public Workflow getWorkflow() {
		return getWorkflow(true);
	}

	public Workflow getWorkflow(boolean create) {
		if (!(create || stateManager.exists(workflowName))) {
			return null;
		}
		return (Workflow) stateManager.get(workflowName);
	}

	/*
	 * Utils
	 */

	private boolean workflowExists() {
		return stateManager.exists(workflowName);
	}

	private Map<String, Object> currentContext() {
		return getWorkflow().getCurrentEntry().getContext();
	}

	private String buildKey(String objectName) {
		return "state:" + objectName;
	}

	/*
	 * Paramètres
	 */

	public void setStateManager(ApplicationStateManager stateManager) {
		this.stateManager = stateManager;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

}
