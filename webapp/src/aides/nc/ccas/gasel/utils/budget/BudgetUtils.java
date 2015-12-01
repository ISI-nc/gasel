package nc.ccas.gasel.utils.budget;

import org.apache.cayenne.DataRow;

public class BudgetUtils {

	public static Double pourcent(Double partie, Double total) {
		if (total == null)
			return null;
		if (partie == null)
			return 0d;
		return partie * 100d / total;
	}

	public static double doubleValue(DataRow row, String col) {
		Number n = numberValue(row, col);
		if (n == null)
			return 0d;
		return n.doubleValue();
	}

	public static int intValue(DataRow row, String col) {
		Number n = numberValue(row, col);
		if (n == null)
			return 0;
		return n.intValue();
	}

	private static Number numberValue(DataRow row, String col) {
		if (!row.containsKey(col))
			throw new RuntimeException("Colonne inexistante : " + col);

		return (Number) row.get(col);
	}

	public static Double cumule(Double v1, Double v2) {
		if (v1 == null && v2 == null) {
			return null;
		} else if (v1 == null) {
			return v2;
		} else if (v2 == null) {
			return v1;
		}
		return v1 + v2;
	}

}
