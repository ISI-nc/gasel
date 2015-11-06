package nc.ccas.gasel.stats.tr;

import nc.ccas.gasel.stats.TableauStat;
import nc.ccas.gasel.stats.Transformation;
import nc.ccas.gasel.stats.TransformationQuery;

public class SqlTr implements Transformation {

	private final TransformationQuery query;

	public SqlTr(String query) {
		this(new TransformationQuery().definition(query));
	}

	public SqlTr(TransformationQuery query) {
		this.query = query;
	}

	public TransformationQuery getQuery(TableauStat tableau) {
		return query;
	}

}
