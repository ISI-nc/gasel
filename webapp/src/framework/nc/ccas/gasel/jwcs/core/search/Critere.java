package nc.ccas.gasel.jwcs.core.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nc.ccas.gasel.Check;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.SqlUtils;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.AbstractFormComponent;

import com.asystan.common.cayenne.QueryFactory;

public abstract class Critere extends AbstractFormComponent {

	public static <T> List<T> filterList(List<T> source,
			Expression... expressions) {
		return filterList(source, Arrays.asList(expressions));
	}

	public static <T> List<T> filterList(List<T> source, List<Expression> exprs) {
		Expression expr = QueryFactory.createAnd(exprs);
		List<T> results = new ArrayList<T>();
		for (T value : source) {
			if (!expr.match(value))
				continue;
			results.add(value);
		}
		return results;
	}

	/*
	 * ------------------------------------------------------------------------
	 * To be overridden
	 */

	protected abstract void renderImpl(IMarkupWriter writer, IRequestCycle cycle);

	protected Expression buildExpressionImpl(String path) {
		return null;
	}

	public Check<DataObject> postFilter() {
		return null;
	}

	/*
	 * Spécification du filtre
	 */

	@Parameter(required = true, name = "path")
	public abstract List<String> getPathFromSource();

	public static Object readDbAttribute(DataObject obj, String column) {
		if (obj.getObjectId().getIdSnapshot().containsKey(column)) {
			return obj.getObjectId().getIdSnapshot().get(column);
		}
		return ((DataContext) obj.getObjectContext()).getObjectStore()
				.getDataRowCache().getCachedSnapshot(obj.getObjectId())
				.get("codcom");
	}

	@SuppressWarnings("unchecked")
	public final Expression buildExpression(ObjEntity entity) {
		List<Expression> exprs = new LinkedList<Expression>();
		for (String path : getPathFromSource()) {
			Expression expr = buildExpressionImpl(path);
			if (expr == null)
				continue;
			exprs.add(expr);
		}
		if (exprs.isEmpty()) {
			return null;
		}

		// XXX Marche po: Expression expr = QueryFactory.createOr(exprs);
		Expression expr;
		if (exprs.size() > 1) {
			DataContext dc = CayenneUtils.createDataContext();
			Set<Integer> values = new HashSet<Integer>();
			for (Expression expr2 : exprs) {
				List<DataObject> res = dc.performQuery(new SelectQuery(entity,
						expr2));
				for (DataObject obj : res) {
					values.add(DataObjectUtils.intPKForObject(obj));
				}
			}
			expr = SqlUtils.createIn("db:id", values);
		} else {
			expr = exprs.get(0);
		}

		return expr;
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
	}

	/*
	 * Rendu
	 */

	protected void rewindImpl(IMarkupWriter writer, IRequestCycle cycle) {
		renderImpl(writer, cycle);
	}

	@Override
	protected final void renderFormComponent(IMarkupWriter writer,
			IRequestCycle cycle) {
		register(cycle);
		renderImpl(writer, cycle);
	}

	@Override
	protected final void rewindFormComponent(IMarkupWriter writer,
			IRequestCycle cycle) {
		register(cycle);
		rewindImpl(writer, cycle);
	}

	private void register(IRequestCycle cycle) {
		Form<?> form = (Form<?>) cycle.getAttribute(Form.KEY);
		if (form == null) {
			return;
		}
		form.addCritere(this);
	}

	public boolean isHidden() {
		return false;
	}

}
