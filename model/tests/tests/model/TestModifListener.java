package tests.model;

import static java.util.Arrays.asList;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.TraqueModifs;
import nc.ccas.gasel.model.core.Utilisateur;

import org.apache.cayenne.Persistent;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.map.ObjEntity;

public class TestModifListener extends EntityTestCase {

	public static final Set<String> INCLUDE = new TreeSet<String>();
	static {
		// core
		INCLUDE.addAll(asList("ActivitePersonne", "Charge", "Dossier",
				"Personne", "Ressource"));
		// aides
		INCLUDE.addAll(asList("Aide", "AideRefusee", "AspectAides", "Bon",
				"UsageBon"));
		// habitat
		INCLUDE.addAll(asList("ActionSociale", "AideLogement",
				"AspectDossierHabitat", "AspectSIE", "DemandeAffectation",
				"DemandeAideLogement", "Intervention", "ObjectifHabitat",
				"ObjectifProbHabitat", "SecoursImmediatExceptionnel"));
		// paph
		INCLUDE.addAll(asList("AccompagnementPAPH", "AspectDossierPAPH",
				"DemandeTaxi", "DeplacementTaxi", "DossierPAPH",
				"HandicapPAPH", "ObjectifPAPH", "ObjectifProbPAPH",
				"ProcurationPAPH", "SerieTickets", "SpecificiteCartePAPH"));
		// pe
		INCLUDE.addAll(asList("AbsenceAM", "AbsenceEnfant", "AidePaiement",
				"AspectDossierAM", "AspectDossierEnfant",
				"AssistanceMaternelle", "AutorisationEnfant", "EnfantRAM",
				"FormationRAM", "Garde", "HandicapPE", "IntegrationRAM"));
		// pi
		INCLUDE.addAll(asList("ArreteJF", "AspectDossierPI", "AttributionJF",
				"ControleEntretien", "DemandeJF", "Paiement", "Parcelle"));
	}

	public static final Set<String> SKIP = new TreeSet<String>();

	public static Test suite() {
		TestSuite suite = new TestSuite("TraqueModifs implémenté");
		for (ObjEntity entity : entities()) {
			if (SKIP.contains(entity.getName()))
				continue;
			if (!entity.getDbEntity().getSchema().equals("gasel_v2"))
				continue;

			if (!INCLUDE.contains(entity.getName())) {
				if (!isTraqueModifs(entity))
					continue;
			}

			suite.addTest(new TestModifListener(entity.getName()));
		}
		return suite;
	}

	private static boolean isTraqueModifs(ObjEntity entity) {
		return entity.getAttribute("modifDate") != null
				&& entity.getAttribute("modifUtilisateur") != null;
	}

	private static boolean isTraqueModifs(Persistent object) {
		return isTraqueModifs(object.getObjectContext().getEntityResolver()
				.lookupObjEntity(object));
	}

	// --

	public TestModifListener(String entityName) {
		super(entityName);
	}

	@Override
	protected void runTest() throws Throwable {
		DataContext dc = DataContext.createDataContext();
		Persistent object = dc.newObject(entity.getJavaClass().asSubclass(
				Persistent.class));

		if (isTraqueModifs(object)) {
			assertTrue("!TraqueModifs", object instanceof TraqueModifs);
		} else {
			assertTrue("!ModifListener", object instanceof ModifListener);
		}

		if (TraqueModifs.class.isAssignableFrom(entity.getJavaClass())) {
			// Test contrat minimal de TraqueModifs
			Utilisateur user = (Utilisateur) dc.newObject(Utilisateur.class);
			Date date = new Date();

			TraqueModifs instance = (TraqueModifs) entity.getJavaClass()
					.newInstance();
			instance.modified(user, date);

			assertEquals("modifUtilisateur pas mis à jour", //
					user, instance.getModifUtilisateur());
			assertEquals("modifDate pas mis à jour", //
					date, instance.getModifDate());
		}
	}

}
