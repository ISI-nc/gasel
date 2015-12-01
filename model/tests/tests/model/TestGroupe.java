package tests.model;

import static nc.ccas.gasel.modelUtils.CommonQueries.unique;
import junit.framework.TestCase;
import nc.ccas.gasel.model.core.Groupe;

import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.access.DataContext;

public class TestGroupe extends TestCase {

	static {
		AllModelTests.setupDatabase("as400");
	}

	public void testDeletion() throws Exception {
		String libelle = "test " + System.currentTimeMillis() % 10000;

		DataContext context = DataContext.createDataContext();

		Groupe groupe = (Groupe) context.newObject(Groupe.class);
		groupe.setLibelle(libelle);
		groupe.addToAccesPage("Accueil", true);
		groupe.addToUtilisateurs("isinc");

		context.commitChanges();
		assertEquals(PersistenceState.COMMITTED, groupe.getPersistenceState());

		context = DataContext.createDataContext();

		groupe = unique(context, Groupe.class, "libelle", libelle);
		groupe.prepareForDeletion();
		context.deleteObject(groupe);

		context.commitChanges();
		assertEquals(PersistenceState.TRANSIENT, groupe.getPersistenceState());
	}

}
