package nc.ccas.gasel.modelUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataRow;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.parser.ASTDbPath;
import org.apache.cayenne.exp.parser.ASTIn;
import org.apache.cayenne.exp.parser.ASTList;
import org.apache.cayenne.exp.parser.ASTObjPath;
import org.apache.cayenne.exp.parser.ASTPath;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.cayenne.query.SelectQuery;

import com.asystan.common.StringUtils;

public class SqlUtils {

	public static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static String dateToSql(Date date) {
		if (date == null) {
			return "NULL";
		}
		return dateToExpr(date) + "::date";
	}

	public static String dateToExpr(Date date) {
		return "'" + DATE_FORMAT.format(date) + "'";
	}

	public static Expression createIn(String path, Collection<?> values) {
		// throws FalseInExpression {
		if (values.isEmpty()) {
			// Very très moche
			return createIn("db:id", Arrays.asList(-1));
			// throw new FalseInExpression("Je ne peux pas faire un 0=1");
		}
		ASTPath astPath;
		if (path.startsWith("db:")) {
			astPath = new ASTDbPath(path.substring(3));
		} else {
			astPath = new ASTObjPath(path);
		}
		return new ASTIn(astPath, new ASTList(values));
	}

	public static SQLTemplate parametrerTemplate(SQLTemplate template,
			Object... parametres) {
		Map<String, Object> params = new TreeMap<String, Object>();
		for (int i = 0; i < parametres.length; i += 2) {
			params.put((String) parametres[i], parametres[i + 1]);
		}
		return template.queryWithParameters(params);
	}

	public static <T extends DataObject> Map<T, Integer> mapIdCount(
			DataContext context, Class<T> clazz, List<DataRow> rows) {
		Map<Integer, T> objs = new HashMap<Integer, T>();
		for (Object o : context.performQuery(new SelectQuery(clazz))) {
			T t = clazz.cast(o);
			objs.put(DataObjectUtils.intPKForObject(t), t);
		}
		Map<T, Integer> retval = new HashMap<T, Integer>();
		for (DataRow row : rows) {
			int id = (Integer) row.get("id");
			int count = ((Number) row.get("count")).intValue();
			retval.put(objs.get(id), count);
		}
		return retval;
	}

	public static double toDouble(Object object) {
		if (object == null) {
			return 0.0;
		}
		return ((Number) object).doubleValue();
	}

	public static int toInt(Object object) {
		if (object == null) {
			return 0;
		}
		return ((Number) object).intValue();
	}

	public static String templateDateInRange(String table) {
		return table + ".debut <= $fin_mois\n" + "   AND (" + table
				+ ".fin IS NULL OR " + table + ".fin >= $debut_mois)";
	}

	public static String idList(Collection<? extends DataObject> objects) {
		String[] pks = new String[objects.size()];
		int i = 0;
		for (DataObject object : objects) {
			pks[i++] = String.valueOf(DataObjectUtils.intPKForObject(object));
		}
		return StringUtils.join(",", pks);
	}

	public static DataRow rowById(List<DataRow> rows, String colName,
			Persistent object) {
		int match = DataObjectUtils.intPKForObject(object);
		for (DataRow row : rows) {
			if ((Integer) row.get(colName) == match) {
				return row;
			}
		}
		return null;
	}

	public static String escape(String str) {
		return "'" + str.replace("'", "''") + "'";
	}

	public static Integer idOf(DataObject object) {
		if (object.getObjectId().isTemporary())
			return null;
		return DataObjectUtils.intPKForObject(object);
	}

}
