package gasel.maintenance;

import static nc.ccas.gasel.modelUtils.CommonQueries.select;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.AideEau;
import nc.ccas.gasel.model.aides.Arrete;
import nc.ccas.gasel.model.aides.UsageBon;

import org.apache.cayenne.DataObjectUtils;

import tests.model.AllModelTests;

public class ModifierArrete {

	public static void main(String[] args) throws Exception {
		// ------------------------------------------------------------
		AllModelTests.setupDatabase("noumea");
		// ------------------------------------------------------------

		for (Arrete arrete : select(Arrete.class, "numero='2012/197'")) {
			System.out.println(DataObjectUtils.intPKForObject(arrete) + ": "
					+ arrete.getCreation() + " / " + arrete.getMontant());
			// arrete.setEdite(false);
			for (UsageBon usage : arrete.getBonsValides()) {
				Aide aide = usage.getBon().getAide();
				AideEau aideEau = aide.getEau();
				if (aideEau == null)
					continue;
				if (aideEau.getPolice() != null)
					continue;
				
//				System.out.println(aide);
//				System.out.println("  -> " + usage.getBon().getPersonne());
//				if (aide.getId() == 492606) {
//					System.out.println("  -> police n°51.5038438");
//					aideEau.setPolice("51.5038438");
//				} else if (aide.getId() == 502208) {
//					System.out.println("  -> police n°51.5020246");
//					aideEau.setPolice("51.5020246");
//				} else {
//					throw new RuntimeException("n° d'aide inconnu");
//				}
			}
			arrete.getObjectContext().commitChanges();
		}
	}

}
