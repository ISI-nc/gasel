package gasel.maintenance;
import nc.ccas.gasel.model.aides.Arrete;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;
import nc.ccas.gasel.model.aides.Facture;
import nc.ccas.gasel.model.aides.PartieFacture;
import nc.ccas.gasel.model.aides.UsageBon;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.access.DataContext;

public class SupprUsage {

	private static final boolean COMMIT_CHANGES = true;

	public static void main(String[] args) {
		DataContext oc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(oc);

		Bon bon = CommonQueries.unique(oc, Bon.class, "numero", "201104070801");

		System.out.println("Bon " + bon + " : " + bon.getEtat());

		UsageBon usage = bon.getUsage();
		if (usage == null) {
			System.out.println("Bon sans usage.");
			if (bon.getEtat().isUtilise()
					|| bon.getEtat().isPartiellementUtilise()) {
				desutiliserBon(bon);
				if (COMMIT_CHANGES) {
					oc.commitChanges();
				}
			} else {
				System.out.println("Rien à faire.");
			}
			System.exit(0);
		}

		PartieFacture partieFacture = usage.getFacture();
		Facture facture = partieFacture == null ? null : usage.getFacture()
				.getFacture();
		Arrete arrete = usage.getArrete();

		if (facture != null) {
			System.out.println("Bon sur la facture n°" + facture.getNumero()
					+ " (partie " + partieFacture.getLibelle() + ")");
			System.out.println("=> suppression de cette facture.");
			partieFacture.removeFromBons(usage);
		}

		if (arrete != null) {
			System.out.println("Bon sur l'arrêté n°" + arrete.getNumero());
			System.out.println("=> suppression de cet arrêté.");
			arrete.removeFromBonsValides(usage);
		}

		System.out.println();
		System.out.println("=> suppression de l'usage du bon.");
		bon.removeFromUsages(usage);
		oc.deleteObject(usage);

		desutiliserBon(bon);

		if (COMMIT_CHANGES) {
			oc.commitChanges();
		}
	}

	private static void desutiliserBon(Bon bon) {
		System.out.println("=> remise du bon à l'état EDITE.");
		bon.setEtat(EtatBon.EDITE, false);
	}

}
