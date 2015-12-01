package nc.ccas.gasel.workflow;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseBuilder;

public class WorkflowService implements IEngineService {

	public static final String NAME = "workflow";

	private LinkFactory _linkFactory;

	private IWorkflowScopeManager _workflowScopeManager;

	private ResponseBuilder _responseBuilder;

	public ILink getLink(boolean post, Object parameter) {
		Defense.notNull(parameter, "parameter");
		if (parameter instanceof Object[]) {
			parameter = ((Object[]) parameter)[0];
		}
		int entryId;
		if (parameter instanceof Integer) {
			entryId = (Integer) parameter;
		} else if (parameter instanceof WorkflowEntry) {
			entryId = ((WorkflowEntry) parameter).getId();
		} else {
			throw new ApplicationRuntimeException(
					"Parameter to the workflow service must be an Integer or a WorkflowEntry");
		}

		Map<String, String> parameters = new TreeMap<String, String>();
		parameters.put("entry", Integer.toString(entryId));
		return _linkFactory.constructLink(this, post, parameters, true);
	}

	public String getName() {
		return NAME;
	}

	public void service(IRequestCycle cycle) throws IOException {
		int entryId = Integer.parseInt(cycle.getParameter("entry"));
		WorkflowEntry entry = _workflowScopeManager.getWorkflow().get(entryId);
		if (entry == null) { // Timeout
			_workflowScopeManager.getWorkflow().getRoot().redirect(cycle);
			return;
		}
		entry.activate(cycle);
		_responseBuilder.renderResponse(cycle);
	}

	public void setLinkFactory(LinkFactory linkFactory) {
		_linkFactory = linkFactory;
	}

	public void setWorkflowScopeManager(
			IWorkflowScopeManager workflowScopeManager) {
		_workflowScopeManager = workflowScopeManager;
	}

	public void setResponseBuilder(ResponseBuilder builder) {
		_responseBuilder = builder;
	}

}
