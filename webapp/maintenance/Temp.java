import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.ccas.gasel.model.aides.Arrete;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;
import nc.ccas.gasel.model.aides.Facture;
import nc.ccas.gasel.model.aides.PartieFacture;
import nc.ccas.gasel.model.aides.UsageBon;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.pages.arretes.AjoutAidesLigne;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;


public class Temp {

	public static void main(String[] args) {

		DataContext oc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(oc);

		List<Facture> factures = CommonQueries.select(Facture.class, Expression
				.fromString("parties.bons.arrete = null and "
						+ "(parties.bons.bon.etat=" + EtatBon.UTILISE
						+ " or parties.bons.bon.etat="
						+ EtatBon.PARTIELLEMENT_UTILISE + ") and ("
						+ "parties.bons.bon.aide.nature.imputation = "
						+ Imputation.SOLIDARITE
						+ "or parties.bons.bon.aide.nature = " + NatureAide.EAU
						+ "or parties.bons.bon.aide.nature = "
						+ NatureAide.ORDURES_MENAGERES
						+ "or parties.bons.bon.aide.nature.imputation = "
						+ Imputation.AVANCE + ")"));

		List<Facture> selection = new ArrayList<Facture>(factures.size());

		for (Facture facture : factures) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(facture.getCreation());
			if (cal.get(Calendar.YEAR) != 2009)
				continue;

			// if (!facture.getNumero().equals("01047/04/2009"))
			// continue;

			selection.add(facture);
		}

		Arrete arrete = CommonQueries.unique(oc, Arrete.class, "numero",
				"Régul. 2009");

		for (Facture facture : selection) {
			Set<UsageBon> ubs = new HashSet<UsageBon>();
			int found = 0;
			int count = 0;
			for (PartieFacture partie : facture.getParties()) {
				for (UsageBon usage : partie.getBons()) {
					count++;
					if (usage.getArrete() == null) {
						found++;
						ubs.add(usage);
					}
				}
			}

			if (found == count)
				continue; // on passe si c'est 100% hors arrêté

			System.out.println();
			System.out.println("-- -- -- -- -- --");
			System.out.println("Facture :     " + facture.getNumero());
			System.out.println("Fournisseur : "
					+ facture.getFournisseur().getLibelle());
			System.out
					.println("Montant :     " + facture.getMontant() + " XPF");
			// System.out.printf("%-35s %7d %7d (%3d%%)\n", facture.getNumero(),
			// found, count, found * 100 / count);
			System.out.println();

			List<AjoutAidesLigne> aals = AjoutAidesLigne
					.traiteUsages(new ArrayList<UsageBon>(ubs));

			for (AjoutAidesLigne aal : aals) {
				System.out.printf("%-40s %-20s %-10s %6d\n", aal.getAide()
						.getDossier().getDossier().getChefFamille().getRepr(),
						aal.getAide().getStatut().toString(), aal
								.getPeriode(), aal.getMontant());

				for (Bon bon : aal.getBons()) {
					arrete.addToBonsValides(bon.getUsage());
				}
			}

		}
		
		//oc.commitChanges();
	}
}
