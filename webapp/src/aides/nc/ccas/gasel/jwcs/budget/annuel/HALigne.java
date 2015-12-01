package nc.ccas.gasel.jwcs.budget.annuel;

import static nc.ccas.gasel.utils.budget.BudgetUtils.pourcent;
import nc.ccas.gasel.reports.Cumulable;

public class HALigne implements Cumulable<HALigne> {

	private boolean highlight;
	private String libelle;
	private HALigne parent;

	private int[] valeurs = new int[12];

	public HALigne() {
		// skip
	}

	public HALigne(String libelle) {
		this(libelle, false);
	}

	public HALigne(String libelle, boolean highlight) {
		this.libelle = libelle;
		this.highlight = highlight;
	}

	// ------------------------------------------------------------------
	// Cumulable<HALigne>
	//

	public void cumule(HALigne other) {
		for (int i = 0; i < valeurs.length; i++) {
			valeurs[i] += other.getValeur(i);
		}
	}

	// ------------------------------------------------------------------
	// Simple
	//

	public int getTotal() {
		int total = 0;
		for (int valeur : valeurs) {
			total += valeur;
		}
		return total;
	}

	public Double getProportion() {
		if (parent == null)
			return null;
		return pourcent((double) getTotal(), (double) parent.getTotal());
	}

	// ------------------------------------------------------------------
	// Très simple
	//

	public int valeur(int mois) {
		return getValeur(mois);
	}

	public int getValeur(int mois) {
		return valeurs[mois];
	}

	public void setValeur(int mois, int valeur) {
		valeurs[mois] = valeur;
	}

	public void add(int mois, int valeur) {
		valeurs[mois] += valeur;
	}

	// ------------------------------------------------------------------
	// Très très simple
	//

	public boolean isHighlight() {
		return highlight;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public HALigne getParent() {
		return parent;
	}

	public void setParent(HALigne parent) {
		this.parent = parent;
	}

}
