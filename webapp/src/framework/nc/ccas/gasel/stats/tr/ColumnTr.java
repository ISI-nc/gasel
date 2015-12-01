package nc.ccas.gasel.stats.tr;

import nc.ccas.gasel.stats.TableauStat;
import nc.ccas.gasel.stats.Transformation;
import nc.ccas.gasel.stats.TransformationQuery;

public class ColumnTr implements Transformation {

	private final String column;

	public ColumnTr(String column) {
		this.column = column;
	}

	public TransformationQuery getQuery(TableauStat tableau) {
		return new TransformationQuery().definition("$t." + column);
	}

}
