package tests.model;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.cayenne.conf.GaselDataSourceFactory;
import org.apache.cayenne.map.ObjEntity;

import tests.model.budget.AllBudgetTests;

public class AllModelTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests du modèle");
		suite.addTest(cayenneSuite());
		suite.addTest(AllBudgetTests.suite());
		return suite;
	}

	private static Test cayenneSuite() {
		TestSuite suite = new TestSuite("Tests Cayenne");
		suite.addTest(TestClassLoading.suite());
		suite.addTest(TestEntityCreation.suite());
		suite.addTest(TestEntityRetrieve.suite());
		suite.addTest(TestEntityDeletion.suite());
		suite.addTest(TestEnumSync.suite());
		return suite;
	}

	/**
	 * Set {@link GaselDataSourceFactory} override and setupLogging.
	 */
	public static final void setupDatabase() {
		setupDatabase(AllModelTests.class, "fallback-db.properties");
	}

	/**
	 * Set {@link GaselDataSourceFactory} override and setupLogging.
	 */
	public static final void setupDatabase(String dsfName) {
		setupDatabase(AllModelTests.class, dsfName);
	}

	/**
	 * Set {@link GaselDataSourceFactory} override and setupLogging.
	 */
	public static void setupDatabase(Class<?> refClass) {
		setupDatabase(refClass, "fallback-db.properties");
	}

	/**
	 * Set {@link GaselDataSourceFactory} override and setupLogging.
	 */
	public static void setupDatabase(Class<?> refClass, String dsfName) {
		setupLogging();
		GaselDataSourceFactory.setOverride(dsfName);
	}

	public static void setupLogging() {
		System.setProperty("log4j.configuration", url("log4j.properties"));
	}

	public static void disableLogging() {
		System.setProperty("log4j.configuration",
				url("log4j.disable.properties"));
	}

	private static String url(String baseName) {
		String url;
		try {
			url = new File(baseName).toURI().toURL().toExternalForm();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		return url;
	}

	public static void openModelFile(ObjEntity entity) throws IOException,
			InterruptedException {
		Process dia = Runtime.getRuntime().exec(
				new String[] {
						"dia",
						"schemas/"
								+ entity.getDataMap().getName().replace(".",
										"/") + "/model.dia" });
		dia.waitFor();
	}

}
