package tests.model.pi;

import junit.framework.TestCase;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pi.AspectDossierPI;
import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.access.DataContext;

import tests.model.AllModelTests;

public class TestAspect extends TestCase {

	public void testCreation() {
		DataContext dc = CayenneUtils.createDataContext();
		Dossier dossier = (Dossier) DataObjectUtils.objectForPK(dc, Dossier.class, 320);
		AspectDossierPI aspect = dossier.getAspect(AspectDossierPI.class);
		if (aspect != null) {
			dc.deleteObject(aspect);
		}
		dc.commitChanges();

		Utilisateur tf = (Utilisateur) DataObjectUtils.objectForPK(dc,
				Utilisateur.class, 1);

		aspect = (AspectDossierPI) dc.newObject(AspectDossierPI.class);
		aspect.setDossier(dossier);
		aspect.setReferentFamilial(tf);
		dc.commitChanges();

		dc.deleteObject(aspect);
		dc.commitChanges();
	}

}
