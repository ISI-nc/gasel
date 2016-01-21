package nc.ccas.gasel;

import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.pages.habilitations.EditerCompte;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;


public abstract class Comptes extends BasePage {

	private List<Utilisateur> _utilisateursActifs;

	private List<Utilisateur> _utilisateursInactifs;

	public abstract String getLoginToAdd();

	@InjectPage("habilitations/EditerCompte")
	public abstract EditerCompte getPageCompte();

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_utilisateursActifs = null;
		_utilisateursInactifs = null;
	}

	public void creer(IRequestCycle cycle) {
		workflowOpen("habilitations/EditerCompte");
	}

	public List<Utilisateur> getUtilisateursActifs() {
		if (_utilisateursActifs == null) {
			_utilisateursActifs = sort(
					sql.query(Utilisateur.class, "actif <> 0")).by("nom")
					.by("prenom").results();
		}
		return _utilisateursActifs;
	}

	public List<Utilisateur> getUtilisateursInactifs() {
		if (_utilisateursInactifs == null) {
			_utilisateursInactifs = sort(
					sql.query(Utilisateur.class, "actif = 0")).by("nom")
					.by("prenom").results();
		}
		return _utilisateursInactifs;
	}
}