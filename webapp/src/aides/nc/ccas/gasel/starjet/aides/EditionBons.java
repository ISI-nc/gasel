package nc.ccas.gasel.starjet.aides;

import static nc.ccas.gasel.Formats.DATE_FORMAT;
import static nc.ccas.gasel.modelUtils.DateUtils.today;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import nc.ccas.gasel.BasePageSort;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.mairie.FournisseurMairie;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.starjet.AbstractStarjetListPresenter;
import nc.ccas.gasel.starjet.StarjetWriter;

public class EditionBons extends AbstractStarjetListPresenter<Bon> {

	private static final DateFormat JOUR_SEMAINE = new SimpleDateFormat("EEEE");

	public EditionBons() {
		super(Bon.class);
	}

	@Override
	public String getStarpageFile() {
		return "bon";
	}

	@Override
	protected void writeImplImpl(PrintWriter out, List<Bon> bons)
			throws IOException {
		if (bons.isEmpty()) {
			throw new Error("Pas de bons.");
		}
		for (Bon bon : bons) {
			if (!bon.getEtat().isEdite())
				throw new Error("Bon non édité envoyé à l'impression : "
						+ bon.getNumero());
		}

		boolean firstPage = true;

		CommonQueries.prefetch(bons.get(0).getObjectContext(), bons, "aide",
				"aide.dossier", "personne", "aide.nature",
				"aide.nature.fournisseurs");

		for (Bon bon : new BasePageSort<Bon>(bons).by("numero").results()) {
			if (firstPage)
				firstPage = false;
			else
				writer(out).pageBreak();

			writeBon(out, bon);
		}
	}

	private void writeBon(PrintWriter out, Bon bon) {
		StarjetWriter w = writer(out);
		w.write("Bon n°: ").write(bon.getNumero()).ln();
		w.write("n° bon code: ").write(coder(bon.getNumero())).ln();
		w.write("Action: ").write(bon.getAide().getCode()).write(" ").write(
				bon.getAide().getNature()).ln();
		w.write("Programme: ").write(
				bon.getAide().getNature().getImputation().getId()).ln();
		w.printf("Date: %8s %s", JOUR_SEMAINE.format(today()),
				DATE_FORMAT.format(today())).ln();
		w.write("Dossier n°: ").writeId(bon.getAide().getDossier()).ln();

		Personne personne = bon.getPersonne();
		w.write("Nom: ").write(personne.getNom()).write(" ").write(
				personne.getPrenom()).ln();
		w.write("Montant: ").write(bon.getMontant()).ln();
		w.write("Date debut: ").date(bon.getDebut()).ln();
		w.write("Date fin: ").date(bon.getFin()).ln();

		// Fournisseurs, 2 par ligne, 6 lignes mini.
		int i = 0;
		for (FournisseurMairie f : bon.getAide().getNature().getFournisseurs()) {
			if (i % 2 == 0) {
				w.printf("%-45s", f.getLibelle());
			} else {
				w.write(f.getLibelle()).ln();
			}
			i++;
		}
		// On fini les lignes (équivalent à 12 fournisseurs)
		if (i > 12 && i % 2 != 0) {
			w.ln();
		} else {
			for (; i < 12; i += 2)
				w.ln();
		}
	}

}
