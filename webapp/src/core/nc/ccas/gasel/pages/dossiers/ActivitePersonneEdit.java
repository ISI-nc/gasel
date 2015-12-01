package nc.ccas.gasel.pages.dossiers;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.core.ActivitePersonne;
import nc.ccas.gasel.model.core.Personne;

public abstract class ActivitePersonneEdit extends EditPage<ActivitePersonne> {

	@Override
	public void prepareNewObject(ActivitePersonne object) {
		if (getParent() == null) {
			throw new RuntimeException(
					"Impossible de créer une activité : pas de personne");
		}
	}

	public void activate(Personne personne) {
		activateForParent(personne, "personne");
	}

	public ActivitePersonneEdit() {
		super(ActivitePersonne.class);
	}

	public String getTitre() {
		if (getObject() == null) {
			return "?";
		}
		String lib = getObject().getLibelle();
		if (lib == null) {
			lib = "?";
		}
		return "Activité " + lib + objectStatus();
	}

	// Alias pour getObject

	public ActivitePersonne getActivite() {
		return getObject();
	}

	public void setActivite(ActivitePersonne activite) {
		setObject(activite);
	}

}
