package tests.model.aides;

import junit.framework.TestCase;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;
import nc.ccas.gasel.model.aides.Facture;
import nc.ccas.gasel.model.aides.PartieFacture;
import nc.ccas.gasel.model.aides.UsageBon;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.access.DataContext;

public class TestRetourBon extends TestCase {

	public void testRetourBon() {
		DataContext dc = DataContext.createDataContext();
		Facture facture = (Facture) DataObjectUtils.objectForPK(dc,
				Facture.class, 260);
		PartieFacture partie = facture.getParties().get(0);

		Bon bon = CommonQueries.unique(dc, Bon.class, "numero", "833124233582");
		partie.ajouterBon(bon);

		UsageBon usage = partie.getBons().get(0);
		assertNotNull(usage.getBon());
		assertEquals(usage, bon.getUsage());

		dc.commitChanges();

		// Undo
		partie.removeFromBons(usage);
		bon.setEtat(EtatBon.EDITE);
		dc.deleteObject(usage);
		dc.commitChanges();
	}

}
