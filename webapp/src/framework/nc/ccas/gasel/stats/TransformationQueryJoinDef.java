package nc.ccas.gasel.stats;

public class TransformationQueryJoinDef {
	private final String table;
	private final String alias;
	private final String onClause;

	public TransformationQueryJoinDef(String table, String alias,
			String onClause) {
		this.table = table;
		this.alias = alias;
		this.onClause = onClause;
	}

	/**
	 * @return La clause de jointure correspondante.
	 */
	public String sql() {
		return table + " " + alias + " ON (" + onClause + ")";
	}

	// -----

	public String getTable() {
		return table;
	}

	public String getAlias() {
		return alias;
	}

	public String getOnClause() {
		return onClause;
	}

}
