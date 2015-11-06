package nc.ccas.gasel.starjet.aides;

import static java_gaps.NumberUtils.add;
import static java_gaps.NumberUtils.sub;

import java.io.IOException;
import java.io.PrintWriter;

import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.AideEau;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.starjet.StarjetWriter;

public class CourrierAideEau extends CourrierAide {

	private final TypeCourrierAide type;

	private final boolean partiel;

	public CourrierAideEau(TypeCourrierAide type, boolean partiel) {
		this.type = type;
		this.partiel = partiel;
	}

	public CourrierAideEau(TypeCourrierAide type) {
		this(type, false);
	}

	@Override
	public String getStarpageFile() {
		if (TypeCourrierAide.COURRIER_ADMINISTRE == type) {
			if (partiel) {
				return "courrieradmin_parti_caledoniennedeseaux";
			} else {
				return "courrierAdmin_CaledonienneDesEaux";
			}
		}
		if (TypeCourrierAide.FAX_FOURNISSEUR == type)
			return "fax_CaledonienneDesEaux";
		throw new IllegalArgumentException(type.toString());
	}

	@Override
	protected void writeImpl(PrintWriter out, Aide aide) throws IOException {
		StarjetWriter w = writer(out);

		// Infos courrier
		dateCourrier(w);

		// Infos aide
		AideEau a = aide.getEau();
		nom(aide, w);
		w.printf("Periode: %s", a.getPeriodePrestation()).ln();
		w.printf("Prise en charge conso.: %-10s",
				montant(a.getPriseEnChargeConso())).ln();
		w.printf("Dépassement: %-4s", a.getDepassementM3()).ln();
		w.printf("Restant dû: %-10s", montant(a.getRestantDu())).ln();
		// Faute de frappe de la v1, pas sûr qu'il faille corriger
		w.printf("Remise compeur: %-10s", montant(remiseCompteur(aide))).ln();
		w.printf("Prise en charge totale: %-10s", montant(aide.getMontant()))
				.ln();
		// Créateur de l'aide (spécifié ainsi, la v1 renvoi l'utilisateur logué)
		Utilisateur u = aide.getModifUtilisateur();
		w.printf("Responsable: %s %s", u.getNom(), u.getPrenom()).ln();

		// Tableau
		Personne cf = aide.getDossier().getDossier().getChefFamille();
		String pattern = "%-50s%-26s%-26s%26s%26s%26s%26s";
		w.printf(pattern, //
				"Nom et prénom", "Police", "Période",
				"Montant de la facture", "Déjà payé", "Pris par le CCAS",
				"Reste du").ln();
		w.ln();
		w.printf(
				pattern, //
				String.format("%s %s %s", cf.getDesignation(), cf.getPrenom(),
						cf.getNom()), // Nom et prénom
				a.getPolice(), // Police
				a.getPeriodePrestation(), // Période
				montant(montantFacture(a)), // Montant de la facture
				montant(a.getMontantDejaPaye()), // Déjà payé
				montant(a.getPriseEnChargeConso()), // Pris par le CCAS
				montant(a.getRestantDu()) // Reste du
				).ln();

		w.printf("%-102s%26s%26s%26s%26s", "Prise en charge remise compteur",
				remiseCompteur(aide), 0, remiseCompteur(aide), 0).ln();
		w.ln();
		w.printf("%-154s%26s", "Total de la prise en charge",
				montant(aide.getMontant())).ln();

		adresse(aide.getDossier().getDossier(), w);
	}

	private Number montantFacture(AideEau a) {
		return add(a.getPriseEnChargeConso(), a.getRestantDu());
	}

	private Number remiseCompteur(Aide aide) {
		return sub(aide.getMontant(), aide.getEau().getPriseEnChargeConso());
	}

	@Override
	protected String translateDesignation(String designation) {
		return CourrierUtils.designationLongue(designation);
	}

}
