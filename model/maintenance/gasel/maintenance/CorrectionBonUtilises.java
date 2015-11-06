package gasel.maintenance;

import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.Arrete;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;
import nc.ccas.gasel.model.aides.PartieFacture;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.budget.SecteurAide;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.access.DataContext;

public class CorrectionBonUtilises {

	public static void main(String[] args) {
		DataContext oc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(oc);

		// for (String numeroBon : new String[] { "201112044802" }) {
		// Bon bon = CommonQueries.unique(oc, Bon.class, "numero", numeroBon);
		for (Bon bon : CommonQueries.select(oc, Bon.class,
				"numero like '2012060%' and " + "(etat = " + EtatBon.UTILISE
						+ " or etat = " + EtatBon.PARTIELLEMENT_UTILISE + ")")) {
			// Facture facture = CommonQueries.unique(oc, Facture.class,
			// "numero", "2011-12/334-LT");
			// PartieFacture partieFacture = facture.getParties().get(0);
			// partieFacture.ajouterBon(bon);
			// oc.commitChanges();

			if (bon.getUsage() != null)
				continue;

			System.out.println("Bon " + bon + " : " + bon.getEtat());

			bon.setEtat(EtatBon.EDITE, false);
			oc.commitChanges();
			System.out.println("  --> remis à EDITE");
			if (true)
				continue;

			Aide a = bon.getAide();
			TypePublic p = a.getPublic();
			NatureAide n = a.getNature();
			SecteurAide s = n.getParent();
			System.out.println(" - " + p);
			System.out.println(" - " + s + " / " + n);
			System.out.println(" - usage: " + bon.getUsage());

			if (bon.getUsage() != null
					&& (bon.getEtat().isUtilise() || bon.getEtat()
							.isPartiellementUtilise())) {
				Arrete arrete = bon.getUsage().getArrete();
				PartieFacture facture = bon.getUsage().getFacture();
				System.out.println("--> bon utilisé");
				System.out.println(" `--> usage:   " + bon.getUsage());
				System.out.println(" `--> facture: "
						+ (facture == null ? null : facture.getFacture()
								.getNumero()));
				System.out.println(" `--> arrete:  "
						+ (arrete == null ? null : arrete.getNumero()));
			}
			System.out.println();
		}
	}

}
