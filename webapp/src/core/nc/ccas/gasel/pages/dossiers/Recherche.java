package nc.ccas.gasel.pages.dossiers;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.Check;
import nc.ccas.gasel.Formats;
import nc.ccas.gasel.ObjectCallbackPage;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.state.dossiers.DossierPersonne;

import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;

public abstract class Recherche extends ObjectCallbackPage<Dossier> {

	@Persist("workflow")
	public abstract String getAspect();

	public abstract void setAspect(String aspect);

	@Persist("workflow")
	public abstract boolean getFiltreAspect();

	public abstract void setFiltreAspect(boolean filtreActif);

	@Persist("workflow")
	public abstract String getNomCF();

	public abstract void setNomCF(String nom);

	@InjectPage("dossiers/Edition")
	public abstract Edition getPageEdition();

	public String getNomAspect() {
		for (List<String> def : getPageEdition().getAspects()) {
			if (getAspect().equals(def.get(1))) {
				return def.get(0);
			}
		}
		return getAspect();
	}

	public List<DossierPersonne> filtrer(List<Dossier> dossiers) {
		if (getAspect() != null && getFiltreAspect()) {
			dossiers = lists.filter(dossiers, new Check<Dossier>() {
				public boolean check(Dossier dossier) {
					return dossier.getAspect(getAspect()) != null;
				}
			});
		}

		List<DossierPersonne> retval = new ArrayList<DossierPersonne>();
		for (Dossier dossier : dossiers) {
			for (Personne personne : dossier.getPersonnes()) {
				retval.add(new DossierPersonne(dossier, personne));
			}
		}
		Collections.sort(retval);
		return retval;
	}

	@Persist("workflow")
	@InitialValue("true")
	public abstract boolean getSearchChefFamille();

	@Persist("workflow")
	@InitialValue("true")
	public abstract boolean getSearchConjoint();

	@Persist("workflow")
	@InitialValue("false")
	public abstract boolean getSearchACharge();

	@Persist("workflow")
	@InitialValue("false")
	public abstract boolean getSearchNonACharge();

	public List<String> pathsPersonne(List<String> subPaths) {
		List<String> paths = new LinkedList<String>();
		for (String subPath : subPaths) {
			paths.addAll(pathsPersonne(subPath));
		}
		return paths;
	}

	public List<String> pathsPersonne(String subPath) {
		List<String> paths = new LinkedList<String>();

		if (getSearchChefFamille())
			paths.add("chefFamille." + subPath);

		if (getSearchConjoint())
			paths.add("conjoint." + subPath);

		if (getSearchACharge())
			paths.add("personnesACharge." + subPath);

		if (getSearchNonACharge())
			paths.add("personnesNonACharge." + subPath);

		return paths;
	}

	public NumberFormat getFormatNumero() {
		return Formats.NUMERO_DOSSIER;
	}

	// public String getAspectSql() {
	// if (getAspect() == null)
	// return null;
	// String table = getObjectContext().getEntityResolver().lookupObjEntity(
	// getAspect()).getDbEntity().getFullyQualifiedName();
	// return "IN (SELECT dossier_id FROM " + table + ")";
	// }

}
