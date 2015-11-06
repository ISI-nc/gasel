package nc.ccas.gasel.pages;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import nc.ccas.gasel.BasePage;

import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.DataRow;
import org.apache.tapestry.annotations.Persist;

public abstract class Query extends BasePage {

	public abstract String getQuery();

	public abstract void setQuery(String query);

	public abstract String getPostTri();

	@Persist("workflow")
	public abstract String getError();

	public abstract void setError(String error);

	@Persist("workflow")
	public abstract List<DataRow> getResults();

	public abstract void setResults(List<DataRow> rows);

	public void open(String query) {
		prepareActivation();
		setQuery(query);
		executeQuery();
	}

	public void executeQuery() {
		List<DataRow> rows;
		String error;
		try {
			rows = sql.query().rows(getQuery());
			error = null;
		} catch (CayenneRuntimeException ex) {
			rows = Collections.emptyList();
			// On remonte à la racine de l'erreur
			Throwable t = ex;
			while (t.getCause() != null)
				t = ex.getCause();
			// et on affecte l'erreur
			error = t.getMessage();
		}
		setResults(rows);
		setError(error);
		redirect();
	}

	public List<DataRow> getRows() {
		List<DataRow> results = getResults();

		if (results == null)
			return null;

		if (getPostTri() != null)
			return sort(results).by(getPostTri()).results();

		return results;
	}

	public List<String> getCols() {
		return sort((Set<String>) getRows().get(0).keySet()).comparable()
				.results();
	}

}
