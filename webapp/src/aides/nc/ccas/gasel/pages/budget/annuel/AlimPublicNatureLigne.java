package nc.ccas.gasel.pages.budget.annuel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.ccas.gasel.agents.budget.result.DossiersPersonnesMontant;
import nc.ccas.gasel.model.core.enums.TypePublic;

import com.asystan.common.AutoBox;

public class AlimPublicNatureLigne implements
		Comparable<AlimPublicNatureLigne>, Serializable {

	private static final long serialVersionUID = -3121374231671269554L;

	private String titre;

	private Map<TypePublic, Integer> dossiers = new HashMap<TypePublic, Integer>();

	private Map<TypePublic, Integer> montants = new HashMap<TypePublic, Integer>();

	public AlimPublicNatureLigne() {
		// skip
	}

	public AlimPublicNatureLigne(String titre) {
		this.titre = titre;
	}

	public int compareTo(AlimPublicNatureLigne o) {
		return titre.compareTo(o.titre);
	}

	public int getTotal() {
		int somme = 0;
		for (Integer v : montants.values()) {
			somme += v;
		}
		return somme;
	}

	public void setQuantite(TypePublic tp, DossiersPersonnesMontant dpm) {
		dossiers.put(tp, dpm.getNbDossiers());
		montants.put(tp, dpm.getMontant());
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public int dossiers(TypePublic tp) {
		return AutoBox.valueOf(dossiers.get(tp));
	}

	public int montants(TypePublic tp) {
		return AutoBox.valueOf(montants.get(tp));
	}

}
