package nc.ccas.gasel.sql;

import static com.asystan.common.cayenne_new.QueryFactory.createEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.SqlParams;

import org.apache.cayenne.exp.Expression;

import com.asystan.common.cayenne_new.QueryFactory;

public class QuickAnd {

	private List<Expression> elements = new LinkedList<Expression>();

	public QuickAnd append(Expression expr) {
		elements.add(expr);
		return this;
	}

	public QuickAnd append(String exprString) {
		return append(exprString, null);
	}

	public QuickAnd append(String exprString, Map<String, ?> params) {
		Expression expr = Expression.fromString(exprString);
		if (params != null) {
			expr = expr.expWithParameters(params);
		}
		return append(expr);
	}

	public QuickAnd equals(String path, Object value) {
		return append(createEquals(path, SqlParams.valueOf(value)));
	}

	public Expression expr() {
		return QueryFactory.createAnd(elements);
	}

	public Expression expression() {
		return expr();
	}

}
