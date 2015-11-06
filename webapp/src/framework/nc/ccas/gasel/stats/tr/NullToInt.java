package nc.ccas.gasel.stats.tr;

import nc.ccas.gasel.stats.TableauStat;
import nc.ccas.gasel.stats.Transformation;
import nc.ccas.gasel.stats.TransformationQuery;

public class NullToInt implements Transformation {

	private final String expression;

	public NullToInt(String expression) {
		this.expression = expression;
	}

	private String getSql() {
		return "CASE WHEN ((" + expression + ") IS NULL) THEN 0 ELSE 1 END";
	}

	public TransformationQuery getQuery(TableauStat tableau) {
		return new TransformationQuery().definition(getSql());
	}

}
