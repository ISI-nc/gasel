package nc.ccas.gasel.pages.parametres.aides;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.MethodObjectCallback;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.mairie.FournisseurMairie;
import nc.ccas.gasel.pages.aides.RechercheFournisseur;

import org.apache.tapestry.annotations.InjectPage;

public abstract class NatureAideEdit extends EditPage<NatureAide> {

	public NatureAideEdit() {
		super(NatureAide.class);
	}

	@InjectPage("aides/RechercheFournisseur")
	public abstract RechercheFournisseur getPageRechercheFournisseur();

	@Override
	protected void prepareNewObject(NatureAide object) {
		object.setActif(true);
		object.setLocked(false);
	}
	
	@Override
	protected void prepareCommit() {
		NatureAide nature = getObject();
		if (nature.getCode() == null) {
			nature.setCode(nature.getParent().getNextCode());
		}
	}

	public void ajouterFournisseur() {
		getPageRechercheFournisseur().activate(
				new MethodObjectCallback(this, "ajouterFournisseur"));
	}

	public void ajouterFournisseur(FournisseurMairie fournisseur) {
		getObject().addToFournisseurs(fournisseur);
	}

}
