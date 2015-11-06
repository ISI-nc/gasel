package nc.ccas.gasel.cayenne;

import org.apache.cayenne.exp.Expression;

import com.asystan.common.cayenne.QueryFactory;


public class MairieExpr {

	public static Expression createTrue(String field) {
		return QueryFactory.createTrue(field);
	}

	public static Expression createFalse(String field) {
		return QueryFactory.createNot(createTrue(field));
	}

	public static String trueValue() {
		return "true";
	}

}
