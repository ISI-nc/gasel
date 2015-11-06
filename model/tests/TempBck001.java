import static nc.ccas.gasel.modelUtils.CommonQueries.unique;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.ccas.gasel.model.aides.Arrete;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.Facture;
import nc.ccas.gasel.model.aides.PartieFacture;
import nc.ccas.gasel.model.aides.UsageBon;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.access.DataContext;

import tests.model.AllModelTests;

public class TempBck001 {

	public static void main(String[] args) {
		DataContext oc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(oc);

		Arrete arrete = CommonQueries.unique(oc, Arrete.class, "numero",
				"2009/83");

		if (false) {
			// "arrivée le 29/01/2008",
			for (String numeroFacture : new String[] { "05/2009 1B",
					"32/UV/09", "33/UV/09", "39/UV/09", "40/UV/09", "01/UV/08",
					"facture bon n°200904081001", "2009-05-62", "A346302",
					"A346819", "A343495", "2092165", "2091562", "A338045",
					"8991", "2004240", "4002050164", "4002051454",
					"4002050165", "4002050175", "4002051289", "4002051409",
					"4002051923", "M 4815", "IC32151" }) {
				Facture facture = CommonQueries.unique(oc, Facture.class,
						"numero", numeroFacture);
				for (PartieFacture partie : facture.getParties()) {
					for (UsageBon usage : partie.getBons()) {
						arrete.addToBonsValides(usage);
					}
				}
			}

			arrete.addToBonsValides(unique(oc, Bon.class, "numero",
					"200902081601").getUsage());

			oc.commitChanges();
		}

		Map<Facture, Set<UsageBon>> factures = new HashMap<Facture, Set<UsageBon>>();

		for (UsageBon bon : arrete.getBonsValides()) {
			Facture facture = bon.getFacture().getFacture();
			Set<UsageBon> bonsFacture = factures.get(facture);
			if (bonsFacture == null) {
				bonsFacture = new HashSet<UsageBon>();
				factures.put(facture, bonsFacture);
			}
			bonsFacture.add(bon);
		}

		System.out.println(factures.size() + " factures.");
		System.out.println("Montant : " + arrete.getMontant());
		System.out.println();

		for (Map.Entry<Facture, Set<UsageBon>> entry : factures.entrySet()) {
			Set<UsageBon> usages = entry.getValue();
			int montant = 0;
			for (UsageBon usage : usages)
				montant += usage.getMontantUtilise();
			System.out.printf("%-20s %10d %10d %s\n", entry.getKey()
					.getNumero(), usages.size(), montant, usages.iterator()
					.next().getBon().getAide().getNature());
		}

		// for (String numero : new String[] { "200902033901", "200902059101",
		// "200902057701", "200902058601", "200902049901", "200902070401",
		// "200902056501", "200902073501", "200902053301", //
		// "200903083601", "200903076301", "200903071101" }) {
		// Bon bon = unique(oc, Bon.class, "numero", numero);
		// System.out.printf("%-30s %-22s %s %s\n",
		// bon.getPersonne().getRepr(), bon.getAide().getNature()
		// .getParent(), bon.getNumero(),
		// (bon.getUsage() == null
		// || bon.getUsage().getArrete() == null ? "null"
		// : bon.getUsage().getArrete().getNumero()));
		// }
	}
}
