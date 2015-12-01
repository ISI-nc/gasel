package tests.model.pe;

import static nc.ccas.gasel.modelUtils.CayenneUtils.createDataContext;
import static nc.ccas.gasel.modelUtils.CommonQueries.findById;
import static nc.ccas.gasel.modelUtils.CommonQueries.unique;
import junit.framework.TestCase;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.pe.AspectDossierEnfant;
import nc.ccas.gasel.model.pe.EnfantRAM;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.access.DataContext;

import tests.model.AllModelTests;

public class TestAspectDossierPE extends TestCase {

	static {
		AllModelTests.setupDatabase("as400");
	}

	public void testFindEnfantFromPersonne() throws Exception {
		DataContext context = createDataContext();

		AspectDossierEnfant enfants = unique(context,
				AspectDossierEnfant.class, "dossier.chefFamille.nom", "cluseau");

		Dossier dossier = findById(Dossier.class, //
				DataObjectUtils.intPKForObject(enfants), //
				createDataContext());

		for (EnfantRAM enfant : enfants.getEnfantsRAM()) {
			assertTrue(enfants.getDossier().getPersonnes() //
					.contains(enfant.getPersonne()));

			assertNotNull(enfants.findEnfantRAM(enfant.getPersonne()));
		}

		assertNotNull(enfants.findEnfantRAM(dossier.getPersonnesACharge()
				.get(0)));
	}

}
