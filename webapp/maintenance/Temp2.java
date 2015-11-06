import static nc.ccas.gasel.modelUtils.CommonQueries.unique;

import java.util.Set;
import java.util.TreeSet;

import nc.ccas.gasel.model.aides.Facture;
import nc.ccas.gasel.model.aides.PartieFacture;
import nc.ccas.gasel.model.aides.UsageBon;
import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.access.DataContext;


public class Temp2 {

	public static void main(String[] args) {

		DataContext oc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(oc);

		for (String numero : new String[] { "01049/06/2009", "2068407" }) {
			Facture facture = unique(oc, Facture.class, "numero", numero);

			int surArrete = 0;
			int count = 0;
			Set<String> arretes = new TreeSet<String>();
			for (PartieFacture pf : facture.getParties()) {
				for (UsageBon usage : pf.getBons()) {
					count++;
					if (usage.getArrete() == null)
						continue;
					arretes.add(usage.getArrete().getNumero());
					surArrete++;
				}
			}

			System.out.println("Facture n°" + numero + " (" + count
					+ " bon(s), dont " + surArrete + " sur un arrêté) :");
			for (String numArrete : arretes) {
				System.out.println(" - " + numArrete);
			}
			System.out.println();
		}
	}
}
