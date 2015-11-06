import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.access.DataContext;

import tests.model.AllModelTests;

public class ModifAide {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		AllModelTests.setupDatabase("noumea");

		DataContext oc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(oc);

		Bon bon = CommonQueries.unique(oc, Bon.class, "numero", "201009059901");

		System.out.println("Bon " + bon + " : " + bon.getEtat());

		System.out.println(bon.getAide().getNature());

	}

}
