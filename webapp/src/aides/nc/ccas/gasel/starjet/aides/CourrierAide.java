package nc.ccas.gasel.starjet.aides;

import java.io.IOException;
import java.io.PrintWriter;

import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.core.Adresse;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.starjet.AbstractStarjetPresenter;
import nc.ccas.gasel.starjet.StarjetWriter;

public class CourrierAide extends AbstractStarjetPresenter<Aide> {

	public CourrierAide() {
		super(Aide.class);
	}

	@Override
	public String getStarpageFile() {
		return "CourrierOM";
	}

	@Override
	protected void writeImpl(PrintWriter out, Aide aide) throws IOException {
		StarjetWriter w = writer(out);

		// Infos courrier
		dateCourrier(w);

		// Infos dossier
		nom(aide, w);

		// Infos aides
		montant(aide, w);
	}

	protected String translateDesignation(String designation) {
		return String.format("%-4s", designation);
	}

	protected void dateCourrier(StarjetWriter w) {
		w.printf("Date: %s", CourrierUtils.dateCourrier()).ln();
	}

	protected void nom(Aide aide, StarjetWriter w) {
		Personne cf = aide.getDossier().getDossier().getChefFamille();
		w.printf("Nom: %s %s %s", translateDesignation(cf.getDesignation()),
				cf.getPrenom(), cf.getNom()).ln();
	}

	protected void montant(Aide aide, StarjetWriter w) {
		w.printf("Montant: %s", montant(aide.getMontant())).ln();
	}

	protected String montant(Number montant) {
		return CourrierUtils.montant(montant);
	}

	protected void adresse(Dossier dossier, StarjetWriter w) {
		if (exploitable(dossier.getAdressePostale())) {
			// Adresse postale
			adresse(w, dossier.getAdressePostale(), true);

		} else if (dossier.getAdresseHabitation() != null) {
			// pas d'adresse postale: on prend l'adresse d'habitation
			adresse(w, dossier.getAdresseHabitation(), true);
		}
	}

	private void adresse(StarjetWriter w, Adresse a, boolean allowBoitePostale) {
		if (allowBoitePostale && a.getBoitePostale() != null) {
			w.printf("BP%s", a.getBoitePostale()).ln();
		} else {
			w.printf("%s %s", //
					ifnull(a.getNumero(), ""), //
					ifnull(a.getRue(), "")).ln();
		}

		if (a.getAutres() != null) {
			w.write(a.getAutres()).ln();
		}

		// En v1 on a un "||", bizarre.
		if (a.getCodePostal() != null && a.getVille() != null) {
			w.printf("%s %s", //
					ifnull(a.getCodePostal(), ""), //
					ifnull(a.getVille(), "")).ln();
		}
	}

	private String ifnull(Object o, String valueIfNull) {
		return o == null ? valueIfNull : String.valueOf(o);
	}

	private static boolean exploitable(Adresse adressePostale) {
		return adressePostale != null
				&& adressePostale.getBoitePostale() != null;
	}

}
