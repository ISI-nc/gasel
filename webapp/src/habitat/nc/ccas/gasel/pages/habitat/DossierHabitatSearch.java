package nc.ccas.gasel.pages.habitat;

import static com.asystan.common.cayenne.QueryFactory.createContains;

import java.util.List;

import nc.ccas.gasel.ObjectCallbackPage;
import nc.ccas.gasel.model.habitat.AspectDossierHabitat;

import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;

import com.asystan.common.cayenne.QueryFactory;
import com.asystan.common.lists.ListUtils;

public abstract class DossierHabitatSearch extends
		ObjectCallbackPage<AspectDossierHabitat> {

	private List<AspectDossierHabitat> _results;

	@Persist("workflow")
	public abstract String getSearchTerm();

	public abstract void setSearchTerm(String searchTerm);

	public List<AspectDossierHabitat> getSearchResults() {
		if (_results == null) {
			String searchTerm = getSearchTerm();
			if (searchTerm == null) {
				return null;
			}
			searchTerm = searchTerm.trim();
			Expression expr = QueryFactory.createOr( //
					createContains("dossier.chefFamille.nom", searchTerm), // 
					createContains("dossier.chefFamille.prenom", searchTerm));
			SelectQuery query = new SelectQuery(AspectDossierHabitat.class,
					expr);
			_results = ListUtils.cast(AspectDossierHabitat.class,
					getObjectContext().performQuery(query));
		}
		return _results;
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_results = null;
	}

}
