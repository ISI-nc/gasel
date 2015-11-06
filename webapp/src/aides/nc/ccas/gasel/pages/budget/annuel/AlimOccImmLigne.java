package nc.ccas.gasel.pages.budget.annuel;

import java.io.Serializable;

import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.reports.Cumulable;
import nc.ccas.gasel.reports.aides.InterventionAlim;

import org.apache.cayenne.DataRow;

public class AlimOccImmLigne implements Comparable<AlimOccImmLigne>,
		Cumulable<AlimOccImmLigne>, Serializable {

	private static final long serialVersionUID = 308525415963604241L;

	private String titre;

	private final InterventionAlim occasionnel = new InterventionAlim();

	private final InterventionAlim urgent = new InterventionAlim();

	public AlimOccImmLigne(String titre) {
		this.titre = titre;
	}

	public void fill(DataRow row) {
		switch ((Integer) row.get("freq_id")) {
		case StatutAide.OCCASIONNELLE:
			occasionnel.fill(row);
			break;
		case StatutAide.IMMEDIATE:
			urgent.fill(row);
			break;
		}
	}

	public void cumule(AlimOccImmLigne other) {
		occasionnel.cumule(other.occasionnel);
		urgent.cumule(other.urgent);
	}

	public int compareTo(AlimOccImmLigne o) {
		return titre.compareTo(o.titre);
	}

	public int getTotal() {
		return occasionnel.getMontant() + urgent.getMontant();
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public InterventionAlim getOccasionnel() {
		return occasionnel;
	}

	public InterventionAlim getImmediat() {
		return urgent;
	}

}
