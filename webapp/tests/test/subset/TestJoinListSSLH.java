package test.subset;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nc.ccas.gasel.jwcs.core.edit.subset.JoinListSSLH;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.budget.TypePublicNature;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.access.DataContext;

public class TestJoinListSSLH extends TestCase {

	public void testNatureAidePublics() throws Exception {
		DataContext dc = DataContext.createDataContext();
		DataContext.bindThreadDataContext(dc);

		NatureAide na = CommonQueries.unique(dc, NatureAide.class, //
				"libelle", "1 Denrées alimentaires");
		JoinListSSLH<TypePublic> sslh = new JoinListSSLH<TypePublic>(na,
				"publicsConcernes", "typePublic");

		List<TypePublic> origPublics = new ArrayList<TypePublic>();
		for (TypePublicNature tpn : na.getPublicsConcernes()) {
			origPublics.add(tpn.getTypePublic());
		}

		List<TypePublic> publics = CommonQueries.getAll(TypePublic.class);

		for (TypePublic public1 : publics) {
			if (origPublics.contains(public1)) {
				sslh.remove(public1);
			} else {
				sslh.add(public1);
			}
		}

		dc.commitChanges();

		for (TypePublic public1 : publics) {
			if (origPublics.contains(public1)) {
				sslh.add(public1);
			} else {
				sslh.remove(public1);
			}
		}

		dc.commitChanges();
	}

}
