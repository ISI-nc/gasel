package tests.model;

import static tests.model.EntityTestCase.entities;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import nc.ccas.gasel.modelUtils.EnumerationSync;

import org.apache.cayenne.map.ObjEntity;

public class TestEnumSync extends TestCase {

	static {
		AllModelTests.setupDatabase();
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Synchronisation des enumérations");
		for (ObjEntity entity : entities()) {
			String className = entity.getClassName();
			Class<?> clazz;
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				continue; // On passe, sujet des tests "loading"
			}
			if (!EnumerationSync.syncableClasses().contains(clazz))
				continue;
			suite.addTest(new TestEnumSync(clazz.getName()));
		}
		return suite;
	}

	private final Class<?> clazz;

	public TestEnumSync(String className) {
		super(className);
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void runTest() throws Throwable {
		EnumerationSync.sync(clazz);
	}

}
