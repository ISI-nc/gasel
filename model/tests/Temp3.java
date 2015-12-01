import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.UsageBon;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.access.DataContext;

public class Temp3 {

	public static void main(String[] args) {
		DataContext oc = CommonQueries.createAndBindThreadContext();

		for (String numero : new String[] { "200902069801", "200902081501" }) {
			System.out.println("Numero: " + numero);
			Bon bon = CommonQueries.unique(oc, Bon.class, "numero", numero);
			System.out.println("  - " + bon.getPersonne());
			UsageBon usage = bon.getUsage();
			if (usage != null) {
				System.out.println("  - Facture: "
						+ usage.getFacture().getFacture().getNumero());
				System.out.println("  - Arrêté: "
						+ (usage.getArrete() != null ? usage.getArrete()
								.getNumero() : null));
			}
			System.out.println();
		}
	}

}
