package nc.ccas.gasel.jwcs.core.search.critere;

import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.parser.SimpleNode;

public class RawSqlNode extends SimpleNode {
	private static final long serialVersionUID = 869395080124327089L;

	public static final RawSqlNode SPACE = new RawSqlNode(" ");

	private final String sql;

	public RawSqlNode(String sql) {
		super(-1);
		this.sql = sql;
	}

	@Override
	public int getOperandCount() {
		return 1;
	}

	@Override
	public Object getOperand(int index) {
		switch (index) {
		case 0:
			return sql;
		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	@Override
	public int getType() {
		return -1;
	}

	@Override
	protected Object evaluateNode(Object o) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String getExpressionOperator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Expression shallowCopy() {
		return new RawSqlNode(sql);
	}

	public String getSql() {
		return sql;
	}

}
