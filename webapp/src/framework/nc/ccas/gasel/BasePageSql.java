package nc.ccas.gasel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.DataRow;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.query.SelectQuery;

public class BasePageSql {

	/**
	 * An instance using the thread's data context.
	 */
	public static final BasePageSql INSTANCE = new BasePageSql();

	final BasePage page;

	final ObjectContext context;

	private final BasePageSqlQuery query = new BasePageSqlQuery(this);

	public BasePageSql() {
		page = null;
		context = null;
	}

	public BasePageSql(BasePage page) {
		this.page = page;
		context = null;
	}

	public BasePageSql(ObjectContext context) {
		this.context = context;
		page = null;
	}

	public <T extends Persistent> List<T> query(Class<T> clazz) {
		return query(clazz, (Expression) null);
	}

	@SuppressWarnings("unchecked")
	public <T extends Persistent> List<T> query(Class<T> clazz,
			Expression qualifier, String... prefetches) {
		SelectQuery query = new SelectQuery(clazz, qualifier);
		CommonQueries.addPrefetches(query, clazz, prefetches);
		return dataContext().performQuery(query);
	}

	public <T extends Persistent> List<T> query(Class<T> clazz,
			String expression, String... prefetches) {
		return query(clazz, Expression.fromString(expression), prefetches);
	}

	public List<? extends Persistent> query(String clazz, String expression,
			String... prefetches) {
		return query(resolveClass(clazz), expression, prefetches);
	}

	public BasePageSqlQuery query() {
		return query;
	}

	public BasePageSqlQuery getQuery() {
		return query;
	}

	public Class<? extends DataObject> resolveClass(String clazz) {
		ObjEntity ent = dataContext().getEntityResolver().getObjEntity(clazz);
		if (ent == null) {
			throw new RuntimeException("Pas d'entité trouvée pour " + clazz);
		}
		return ((Class<?>) ent.getJavaClass()).asSubclass(DataObject.class);
	}

	public SqlParams params() {
		return new SqlParams();
	}

	public Integer idOf(Persistent obj) {
		if (obj.getObjectId().isTemporary()) {
			return null;
		}
		return (Integer) DataObjectUtils.intPKForObject(obj);
	}

	protected ObjectContext dataContext() {
		if (page != null)
			return page.getObjectContext();

		if (context != null)
			return context;

		return DataContext.getThreadDataContext();
	}

	// Filtrage

	public <T extends Persistent> List<T> filtrer(Collection<T> source,
			Expression expr) {
		return filtrer(source, new ExprCheck(expr));
	}

	public <T extends Persistent> List<T> filtrer(Collection<T> source,
			Check<? super T> check) {
		ArrayList<T> values = new ArrayList<T>();
		for (T o : source) {
			if (!check.check(o))
				continue;
			values.add(o);
		}
		values.trimToSize();
		return values;
	}

	private class ExprCheck implements Check<Object> {
		private final Expression expr;

		public ExprCheck(Expression expr) {
			this.expr = expr;
		}

		public boolean check(Object value) {
			return expr.match(value);
		}
	}

	// Transformation

	public BasePageSqlTranslator translator(List<DataRow> rows) {
		return new BasePageSqlTranslator(this, rows);
	}

	public <T extends DataObject> Map<Integer, T> pkMap(Collection<T> objects) {
		Map<Integer, T> map = new HashMap<Integer, T>(objects.size());
		for (T object : objects) {
			if (object.getObjectId().isTemporary())
				continue;
			map.put(DataObjectUtils.intPKForObject(object), object);
		}
		return map;
	}

	public <T extends Persistent> Map<Integer, T> idMap(
			Collection<? extends T> objects) {
		return CommonQueries.idMap(objects);
	}

}
