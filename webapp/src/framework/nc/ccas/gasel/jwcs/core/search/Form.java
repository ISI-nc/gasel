package nc.ccas.gasel.jwcs.core.search;

import static nc.ccas.gasel.modelUtils.CommonQueries.canUseJoinPrefetch;
import static nc.ccas.gasel.modelUtils.CommonQueries.expandPrefetch;
import static org.apache.cayenne.query.PrefetchTreeNode.JOINT_PREFETCH_SEMANTICS;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.Check;
import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.map.ObjAttribute;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.query.PrefetchTreeNode;
import org.apache.cayenne.query.SelectQuery;
import org.apache.log4j.Logger;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Parameter;

import com.asystan.common.cayenne_new.QueryFactory;

public abstract class Form<T extends DataObject> extends BaseComponent {

	private static final Logger LOG = Logger.getLogger(Form.class);
	
	public static final String KEY = "SEARCH_FORM";

	public abstract String getEntity();

	public abstract List<T> getSource();

	public abstract void setResults(List<T> results);

	public abstract boolean getShowResults();

	public abstract void setShowResults(boolean value);

	public abstract void setShowError(boolean value);

	public abstract String getOrder();

	/**
	 * Expression appliquée dans tous les recherches.
	 */
	@Parameter
	public abstract Object getExpr();

	/**
	 * Liste des chemins à précharger. Sera étendue (ex: x.y.z préchargera x,
	 * x.y et x.y.z).
	 */
	@Parameter
	public abstract List<String> getPrefetch();

	// Actions

	public void search(IRequestCycle cycle) {
		if (expressions().isEmpty()) {
			setShowError(true);
			setShowResults(false);
			// setExprCache(null);
			return;
		}
		// updateExprCache();
		setShowResults(true);
		getWorkflow().redirect(cycle);
	}

	// @Persist("workflow")
	// public abstract List<Expression> getExprCache();
	//
	// public abstract void setExprCache(List<Expression> exprs);

	// -------

	private void prepareResults() {
		List<Expression> exprs = expressionsMasquees();
		exprs.addAll(expressions());
		if (exprs == null || exprs.isEmpty()) {
			return;
		}
		exprs.add(parseExpr());
		List<T> results;
		if (getSource() != null) {
			results = Critere.filterList(getSource(), exprs);
		} else {
			ObjEntity entity = entity();
			addGenericFilters(exprs, entity);
			Expression expr = QueryFactory.createAnd(exprs);

			SelectQuery query = new SelectQuery(entity, expr);
			if (getOrder() != null) {
				for (String order : getOrder().split(",")) {
					String[] st = order.split(" +");
					boolean asc = st.length == 1
							|| !st[1].equalsIgnoreCase("DESC");
					query.addOrdering(st[0], asc ? true : false);
				}
			}
			List<String> prefetches = getPrefetch();
			if (prefetches != null) {
				Set<String> allPrefetches = new TreeSet<String>();
				for (String prefetch : prefetches) {
					allPrefetches.addAll(expandPrefetch(prefetch));
				}
				for (String prefetch : allPrefetches) {
					LOG.info("Adding prefetch: " + prefetch);
					PrefetchTreeNode prefetchTreeNode = query
							.addPrefetch(prefetch);
					if (canUseJoinPrefetch(entity, prefetch)) {
						prefetchTreeNode.setSemantics(JOINT_PREFETCH_SEMANTICS);
					}
				}
			}
			results = query(query);
		}

		List<T> otherResults = new ArrayList<T>(results.size());
		for (T result : results) {
			boolean add = true;
			for (Check<DataObject> check : checks()) {
				if (!check.check(result)) {
					add = false;
					break;
				}
			}
			if (!add)
				continue;
			otherResults.add(result);
		}
		setResults(otherResults);
	}

	@SuppressWarnings("unchecked")
	private <E> List<E> query(SelectQuery query) {
		return getObjectContext().performQuery(query);
	}

	private List<Check<DataObject>> checks() {
		List<Check<DataObject>> checks = new LinkedList<Check<DataObject>>();
		for (Critere critere : getCriteres()) {
			Check<DataObject> check = critere.postFilter();
			if (check == null)
				continue;
			checks.add(check);
		}
		return checks;
	}

	private void addGenericFilters(List<Expression> exprs, ObjEntity entity) {
		// Filtre générique sur le boolean "actif"
		ObjAttribute attr = (ObjAttribute) entity.getAttribute("actif");
		if (attr != null && attr.getJavaClass() == Boolean.class) {
			exprs.add(0, QueryFactory.createTrue("actif"));
		}
	}

	private List<Expression> expressions() {
		return expressions(false);
	}

	private List<Expression> expressionsMasquees() {
		return expressions(true);
	}

	// @Persist("workflow")
	// public abstract List<Expression> getCachedExpressions();
	// public abstract void setCachedExpressions(List<Expression> expressions);

	private List<Expression> expressions(boolean hidden) {
		ObjEntity entity = entity();
		List<Expression> exprs = new LinkedList<Expression>();
		for (Critere critere : getCriteres()) {
			if (critere.isHidden() != hidden)
				continue;
			Expression e = critere.buildExpression(entity);
			if (e == null)
				continue;
			exprs.add(e);
		}
		return exprs;
	}

	public void addCritere(Critere critere) {
		getCriteres().add(critere);
	}

	private ObjEntity entity() {
		return CayenneUtils.resolveEntity(getEntity());
	}

	private Expression parseExpr() {
		Object expr = getExpr();
		if (expr == null) {
			return null;
		}
		if (expr instanceof String) {
			return Expression.fromString((String) expr);
		}
		return (Expression) expr;
	}

	// -------

	@Override
	protected void prepareForRender(IRequestCycle cycle) {
		if (getSource() == null && getEntity() == null) {
			throw new RuntimeException(
					"Il faut un des deux paramètres : source ou entity.");
		}
		TapestryUtils.storeUniqueAttribute(cycle, KEY, this);
	}

	@InitialValue("new java.util.LinkedList()")
	public abstract List<Critere> getCriteres();

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		if (getShowResults() || expressions().size() > 0) {
			prepareResults();
		}
		super.cleanupAfterRender(cycle);
	}

}
