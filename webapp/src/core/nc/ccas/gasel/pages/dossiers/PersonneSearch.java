package nc.ccas.gasel.pages.dossiers;

import static com.asystan.common.cayenne.QueryFactory.createContains;

import java.util.List;

import nc.ccas.gasel.ObjectCallback;
import nc.ccas.gasel.ObjectCallbackPage;
import nc.ccas.gasel.model.core.Personne;

import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;

import com.asystan.common.cayenne.QueryFactory;
import com.asystan.common.lists.ListUtils;

public abstract class PersonneSearch extends ObjectCallbackPage<Personne> {

	private List<Personne> _results;

	@Persist("workflow")
	public abstract String getSearchTerm();

	public abstract void setSearchTerm(String searchTerm);

	public List<Personne> getSearchResults() {
		if (_results == null) {
			String searchTerm = getSearchTerm();
			if (searchTerm == null) {
				return null;
			}
			searchTerm = searchTerm.trim();
			Expression expr = QueryFactory.createOr( //
					createContains("nom", searchTerm), // 
					createContains("prenom", searchTerm));
			SelectQuery query = new SelectQuery(Personne.class, expr);
			_results = ListUtils.cast(Personne.class, getObjectContext()
					.performQuery(query));
		}
		return _results;
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_results = null;
	}

	private static final ObjectCallback<PersonneSearch, Personne> SUCCESS_CALLBACK = new ObjectCallback<PersonneSearch, Personne>(
			"dossiers/PersonneSearch") {
		private static final long serialVersionUID = -3747691597419227418L;

		public void performCallback(PersonneSearch page, Personne o) {
			page.success(o);
		}
	};

	public void creer(IRequestCycle cycle) {
		((PersonneEdit) cycle.getPage("dossiers/PersonneEdit"))
				.activate(SUCCESS_CALLBACK);
	}
}
