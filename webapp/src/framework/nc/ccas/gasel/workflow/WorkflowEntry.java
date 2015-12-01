package nc.ccas.gasel.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.access.DataContext;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

public class WorkflowEntry implements Serializable, Comparable<WorkflowEntry> {
	private static final long serialVersionUID = 7410322947656643600L;

	private final int id;

	private final Workflow workflow;

	private String pageName;

	private final Map<String, Object> context = new HashMap<String, Object>();

	// Arborescence
	private final WorkflowEntry parent;

	private final List<WorkflowEntry> children = new ArrayList<WorkflowEntry>();

	private String title;

	private DataContext dataContext = CayenneUtils.createDataContext();

	/*
	 * ------------------------------------------------------------------------
	 * Fonctions utiles
	 * ------------------------------------------------------------------------
	 */

	public void activate(IRequestCycle cycle) {
		workflow.setCurrentEntry(this);
		DataContext.bindThreadDataContext(dataContext);
		cycle.getPage(pageName).validate(cycle);
		cycle.activate(pageName);
	}

	public void redirect(IRequestCycle cycle) {
		redirect(cycle, null);
	}

	public void redirect(IRequestCycle cycle, String anchor) {
		workflow.setCurrentEntry(this);
		ILink link = workflowService(cycle).getLink(false, this);
		throw new RedirectException(link.getURL(anchor, true));
	}

	public WorkflowEntry open(String pageName, String title) {
		return new WorkflowEntry(pageName, title, this);
	}

	public boolean contains(WorkflowEntry entry) {
		if (equals(entry)) {
			return true;
		}
		for (WorkflowEntry child : children) {
			if (child.contains(entry)) {
				return true;
			}
		}
		return false;
	}

	public boolean descendantOf(WorkflowEntry entry) {
		for (WorkflowEntry parent = this.parent; parent != null; parent = parent.parent) {
			if (entry.equals(parent)) {
				return true;
			}
		}
		return false;
	}

	public Set<WorkflowEntry> descendants() {
		Set<WorkflowEntry> retval = new HashSet<WorkflowEntry>();
		addDescendantsTo(retval);
		return retval;
	}

	private void addDescendantsTo(Set<WorkflowEntry> values) {
		for (WorkflowEntry child : children) {
			values.add(child);
			child.addDescendantsTo(values);
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * Seul Workflow ou WorkflowEntry devrait créer des entrées.
	 */
	protected WorkflowEntry(String pageName, String title, Workflow workflow) {
		this(pageName, title, workflow, null);
	}

	protected WorkflowEntry(String pageName, String title, WorkflowEntry parent) {
		this(pageName, title, parent.workflow, parent);
	}

	private WorkflowEntry(String pageName, String title, Workflow workflow,
			WorkflowEntry parent) {
		this.pageName = pageName;
		this.title = title;
		this.workflow = workflow;
		this.id = workflow.nextId();
		this.parent = parent;
		if (parent != null) {
			parent.children.add(this);
		}
		workflow.register(this);
	}

	// IComparable

	public int compareTo(WorkflowEntry o) {
		if (workflow != o.workflow) {
			throw new IllegalArgumentException(
					"Workflow entries need to be in the same workflow to be compared");
		}
		return ((Integer) id).compareTo(o.id);
	}

	@Override
	public boolean equals(Object obj) {
		WorkflowEntry o = (WorkflowEntry) obj;
		if (workflow != o.workflow) {
			return false;
		}
		return (id == o.id);
	}

	// ------ utils ------

	private IEngineService workflowService(IRequestCycle cycle) {
		return cycle.getInfrastructure().getServiceMap().getService(
				WorkflowService.NAME);
	}

	//

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		Defense.notNull(title, "title");
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public WorkflowEntry getParent() {
		return parent;
	}

	public List<WorkflowEntry> getChildren() {
		return Collections.unmodifiableList(children);
	}

	protected void removeChild(WorkflowEntry child) {
		if (!children.remove(child)) {
			throw new IllegalArgumentException("WorkflowEntry #" + id
					+ " doesn't contain #" + child.id);
		}
	}

	public Workflow getWorkflow() {
		return workflow;
	}

	public String getPageName() {
		return pageName;
	}

	public DataContext getObjectContext() {
		return dataContext;
	}

	public void setDataContext(DataContext dataContext) {
		this.dataContext = dataContext;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public Object valueFor(String componentPath, String propertyName) {
		return context.get(WorkflowPropertyPersistenceStrategy.buildChangeKey(
				pageName, componentPath, propertyName));
	}

}
