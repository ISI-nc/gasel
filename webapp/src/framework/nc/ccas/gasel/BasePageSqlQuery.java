package nc.ccas.gasel;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import nc.ccas.gasel.bindings.CayenneAllBinding;
import nc.ccas.gasel.bindings.EnumerationBinding;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.modelUtils.SqlUtils;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.DataRow;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.map.DataMap;
import org.apache.cayenne.query.SQLTemplate;

public class BasePageSqlQuery {

	private final BasePageSql sql;

	public final List<QueryRecord> records = new LinkedList<QueryRecord>();

	public BasePageSqlQuery(BasePageSql sql) {
		this.sql = sql;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> objects(SQLTemplate template, SqlParams params) {
		template.setFetchingDataRows(false);
		return sql.dataContext().performQuery(
				template.queryWithParameters(params));
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> objects(Class<T> clazz, SQLTemplate template,
			SqlParams params) {
		template.setFetchingDataRows(false);
		return sql.dataContext().performQuery(
				template.queryWithParameters(params));
	}

	public List<DataRow> rows(SQLTemplate template, Object... params) {
		SQLTemplate paramTpl = SqlUtils.parametrerTemplate(template, params);
		return rows(paramTpl);
	}

	public List<DataRow> rows(SQLTemplate template, Map<String, ?> params) {
		SQLTemplate paramTpl = template.queryWithParameters(params);
		return rows(paramTpl);
	}

	@SuppressWarnings("unchecked")
	private List<DataRow> rows(SQLTemplate template) {
		template.setFetchingDataRows(true);
		List<DataRow> results = sql.dataContext().performQuery(template);
		if (Utils.isTestMode()) {
			// TODO : enregistrement de la requête finale au lieu de simuler...
			records.add(new QueryRecord(compileTemplate(template), results));
		}
		return results;
	}

	private String compileTemplate(SQLTemplate template) {
		String retval = template.getDefaultTemplate();
		for (Object o : template.getParameters().entrySet()) {
			Entry<?, ?> entry = (Entry<?, ?>) o;
			retval = retval.replace("$" + entry.getKey(), (String) entry
					.getValue());
		}
		return retval;
	}

	public List<DataRow> rows(String template) {
		return rows(new SQLTemplate(dataMap(), template, true));
	}

	public List<DataRow> rows(String template, Map<String, ?> params) {
		return rows(new SQLTemplate(dataMap(), template, true), params);
	}

	private DataMap dataMap() {
		return (DataMap) ((DataContext) sql.dataContext()).getParentDataDomain().getDataMaps()
				.iterator().next();
	}

	public <T extends Persistent> List<T> enumeration(Class<T> clazz) {
		return EnumerationBinding.query(sql.dataContext(), clazz);
	}

	public List<? extends Persistent> enumeration(String clazz) {
		return enumeration(sql.resolveClass(clazz));
	}

	/* Récups par id */

	public <T extends Persistent> T byId(Class<T> clazz, int id) {
		return clazz.cast(DataObjectUtils.objectForPK(sql.dataContext(), clazz,
				id));
	}

	public Persistent byId(String objEntityName, int id) {
		return (Persistent) DataObjectUtils
				.objectForPK(sql.dataContext(), objEntityName, id);
	}

	public <T extends Persistent> T byId(Class<T> clazz, DataRow row,
			String colName) {
		return byId(clazz, SqlUtils.toInt(row.get(colName)));
	}

	/* Récup de table (avec cache) */

	public <T extends DataObject> Set<T> all(Class<T> clazz,
			String... prefetches) {
		Set<T> values;
		values = CayenneAllBinding.query(sql.dataContext(), clazz, prefetches);
		return values;
	}

	public Set<? extends DataObject> all(String entityName) {
		return all(sql.resolveClass(entityName));
	}

	/* unique */

	public <T extends Persistent> T unique(Class<T> clazz, Expression qualifier) {
		return unique(clazz, qualifier, null);
	}

	public <T extends Persistent> T unique(Class<T> clazz, String qualifier) {
		return unique(clazz, Expression.fromString(qualifier));
	}

	public Persistent unique(String clazz, String qualifier) {
		return unique(sql.resolveClass(clazz), qualifier);
	}

	public <T extends Persistent> T unique(Class<T> clazz,
			Expression qualifier, Map<String, ?> params) {
		if (params != null) {
			qualifier = qualifier.expWithParameters(params);
		}
		return CommonQueries.unique(sql.dataContext(), clazz, qualifier);
	}

	public <T extends Persistent> T unique(Class<T> clazz, String qualifier,
			Map<String, ?> params) {
		return unique(clazz, Expression.fromString(qualifier), params);
	}

	public Persistent unique(String clazz, String qualifier,
			Map<String, ?> params) {
		return unique(sql.resolveClass(clazz), qualifier, params);
	}

}
