package nc.ccas.gasel.reports.aides;

import static com.asystan.common.AutoBox.valueOf;

import java.io.Serializable;

import nc.ccas.gasel.reports.Cumulable;

import org.apache.cayenne.DataRow;

public class InterventionAlim implements Comparable<InterventionAlim>,
		Cumulable<InterventionAlim>, Serializable {
	private static final long serialVersionUID = -8536263078678943917L;

	private String titre;

	private int nbDossiers;

	private int nbPersonnes;

	private int montant;

	private boolean highlight = false;

	public InterventionAlim() {
		// skip
	}

	public InterventionAlim(String titre) {
		this.titre = titre;
	}

	// Remplissage depuis une DataRow

	public void fill(DataRow row) {
		this.nbDossiers = valueOf((Integer) row.get("nb_dossiers"));
		this.nbPersonnes = valueOf((Integer) row.get("nb_personnes"));
		this.montant = valueOf((Integer) row.get("montant"));
	}

	// ------------------------------

	public void cumule(InterventionAlim other) {
		nbDossiers += other.nbDossiers;
		nbPersonnes += other.nbPersonnes;
		montant += other.montant;
	}

	public int compareTo(InterventionAlim o) {
		return titre.compareTo(o.titre);
	}

	public int getMontant() {
		return montant;
	}

	public void setMontant(int montant) {
		this.montant = montant;
	}

	public int getNbPersonnes() {
		return nbPersonnes;
	}

	public void setNbPersonnes(int nbAdmin) {
		this.nbPersonnes = nbAdmin;
	}

	public int getNbDossiers() {
		return nbDossiers;
	}

	public void setNbDossiers(int nbDossiers) {
		this.nbDossiers = nbDossiers;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public boolean isHighlight() {
		return highlight;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

}
