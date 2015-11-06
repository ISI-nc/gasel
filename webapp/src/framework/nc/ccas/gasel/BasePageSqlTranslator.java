package nc.ccas.gasel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import nc.ccas.gasel.modelUtils.SqlUtils;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.DataRow;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;

public class BasePageSqlTranslator {

	public final List<Map<String, Object>> result;

	private final BasePageSql sql;

	public BasePageSqlTranslator(BasePageSql sql, List<DataRow> rows) {
		this.sql = sql;
		result = new ArrayList<Map<String, Object>>();
		for (DataRow row : rows) {
			Map<String, Object> resRow = new HashMap<String, Object>();
			for (Object oEntry : row.entrySet()) {
				Entry<?, ?> entry = (Entry<?, ?>) oEntry;
				resRow.put((String) entry.getKey(), entry.getValue());
			}
			result.add(resRow);
		}
	}

	@SuppressWarnings("unchecked")
	public BasePageSqlTranslator translate(String key,
			Class<? extends Persistent> clazz) {
		// Récupération des clés
		Set<Integer> keys = new HashSet<Integer>();
		for (Map<String, Object> row : result) {
			Integer value = (Integer) row.get(key);
			if (value == null)
				continue;
			keys.add(value);
		}
		if (keys.isEmpty())
			return this;

		// Association des valeurs aux clés.
		Expression expr = SqlUtils.createIn("db:id", keys);
		List<? extends Persistent> results = sql.dataContext() //
				.performQuery(new SelectQuery(clazz, expr));
		Map<Integer, Object> targetsById = new HashMap<Integer, Object>();
		for (Persistent p : results) {
			targetsById.put(DataObjectUtils.intPKForObject(p), p);
		}

		// Mise à jour du résultat
		// "xxx_id" => "xxx"
		String outputKey = key.substring(0, key.length() - 3);
		for (Map<String, Object> row : result) {
			Integer vKey = (Integer) row.get(key);
			if (vKey == null)
				continue;
			Object value = targetsById.get(vKey);
			row.put(outputKey, value);
		}

		// End
		return this;
	}

}
