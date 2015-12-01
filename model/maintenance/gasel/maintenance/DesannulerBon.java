package gasel.maintenance;

import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.budget.SecteurAide;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.access.DataContext;

import tests.model.AllModelTests;

public class DesannulerBon {

	public static void main(String[] args) {
		// 
		if (true) {
			System.out.println("NON EXECUTE (sécurité enclenchée)");
			System.exit(1);
		}

		DataContext oc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(oc);

		for (String numeroBon : new String[] {
				"201301028801",
				"201301028802",
				"201301028803",
				"201301028804"
				}) {
			Bon bon = CommonQueries.unique(oc, Bon.class, "numero", numeroBon);

			System.out.println("Bon " + bon + " : " + bon.getEtat());
			Aide a = bon.getAide();
			TypePublic p = a.getPublic();
			NatureAide n = a.getNature();
			SecteurAide s = n.getParent();
			System.out.println(" - " + p);
			System.out.println(" - " + s + " / " + n);

			if (bon.getEtat().isAnnule()) {
				bon.setEtat(EtatBon.EDITE, false);
				oc.commitChanges();
				System.out.println("Bon désannulé ;)");
			}
			System.out.println();
		}

	}

}
