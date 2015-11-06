package gasel.maintenance;

import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.access.DataContext;

public class ReinitEditionMensuelle {

	public static void main(String[] args) {
		if (true) {
			System.out.println("NON EXECUTE (sécurité enclenchée)");
			System.exit(1);
		}
		DataContext oc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(oc);

		int cnt = 0;
		
		for (Bon bon : 
		CommonQueries.select(Bon.class,
				"numero like '201208%' and " +
				"etat = " + EtatBon.EDITE + " and " +
				"aide.statut <> " + StatutAide.IMMEDIATE)
				) {
			//bon.setEtat(EtatBon.CREE, false);
			cnt++;
		}

		System.out.println(cnt);

		oc.commitChanges();
	}

}
