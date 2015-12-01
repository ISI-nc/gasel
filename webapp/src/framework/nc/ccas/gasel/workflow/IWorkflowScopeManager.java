package nc.ccas.gasel.workflow;

import org.apache.tapestry.engine.state.StateObjectPersistenceManager;

public interface IWorkflowScopeManager extends StateObjectPersistenceManager {

	public Workflow getWorkflow();

	public Workflow getWorkflow(boolean create);

}
