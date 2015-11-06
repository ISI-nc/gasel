package nc.ccas.gasel.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.record.PropertyChange;
import org.apache.tapestry.record.PropertyChangeImpl;
import org.apache.tapestry.record.PropertyPersistenceStrategy;

public class WorkflowPropertyPersistenceStrategy implements
		PropertyPersistenceStrategy {

	public static String buildChangeKey(String pageName, String idPath,
			String propertyName) {
		StringBuilder buf = new StringBuilder(pageName);
		if (idPath != null) {
			buf.append(",");
			buf.append(idPath);
		}
		buf.append(",");
		buf.append(propertyName);
		return buf.toString();
	}

	public static final String STRATEGY_ID = "workflow";

	private IWorkflowScopeManager workflowScopeManager;

	public void store(String pageName, String idPath, String propertyName,
			Object newValue) {
		Defense.notNull(pageName, "pageName");
		Defense.notNull(propertyName, "propertyName");

		currentContext().put(buildChangeKey(pageName, idPath, propertyName),
				newValue);
	}

	public Collection<?> getStoredChanges(String pageName) {
		Defense.notNull(pageName, "pageName");

		Collection<PropertyChange> result = new ArrayList<PropertyChange>();
		if (!hasWorkflow()) {
			return result;
		}
		Map<String, Object> context = currentContext();
		for (String key : matchingKeys(pageName)) {
			String[] tokens = unpackChangeKey(key);
			String idPath = tokens[1];
			String propertyName = tokens[2];
			result.add(new PropertyChangeImpl(idPath, propertyName, context
					.get(key)));
		}

		return result;
	}

	public void discardStoredChanges(String pageName) {
		if (!hasWorkflow()) {
			return;
		}
		Map<String, Object> context = currentContext();
		for (String key : matchingKeys(pageName)) {
			context.remove(key);
		}
	}

	/**
	 * Does nothing; workflow persistence does not make use of query parameters.
	 */

	public void addParametersForPersistentProperties(ServiceEncoding encoding,
			boolean post) {
	}

	/*
	 * Utils
	 */

	private String[] unpackChangeKey(String changeKey) {
		String[] ret = changeKey.split(",");
		if (ret.length == 2) { // idPath null
			return new String[] { ret[0], null, ret[1] };
		}
		return ret;
	}

	private Map<String, Object> currentContext() {
		return workflowScopeManager.getWorkflow().getCurrentEntry()
				.getContext();
	}

	private boolean hasWorkflow() {
		return workflowScopeManager.getWorkflow(false) != null
				&& workflowScopeManager.getWorkflow().getCurrentEntry() != null;
	}

	private List<String> matchingKeys(String pageName) {
		List<String> result = new ArrayList<String>();
		if (!hasWorkflow()) {
			return result;
		}
		String match = pageName + ",";
		for (String key : currentContext().keySet()) {
			if (key.startsWith(match)) {
				result.add(key);
			}
		}
		return result;
	}

	// -----
	public void setWorkflowScopeManager(
			IWorkflowScopeManager workflowScopeManager) {
		this.workflowScopeManager = workflowScopeManager;
	}

}
