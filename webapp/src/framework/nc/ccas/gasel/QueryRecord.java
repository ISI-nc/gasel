package nc.ccas.gasel;

import java.util.List;

public class QueryRecord {

	private final String query;

	private final List<?> results;

	public QueryRecord(String query, List<?> results) {
		this.query = query;
		this.results = results;
	}

	public String getQuery() {
		return query;
	}

	public List<?> getResults() {
		return results;
	}

}
