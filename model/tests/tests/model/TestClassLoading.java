package tests.model;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.cayenne.map.ObjEntity;

public class TestClassLoading extends EntityTestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Chargement des classes Cayenne");
		for (ObjEntity entity : entities()) {
			suite.addTest(new TestClassLoading(entity.getName()));
		}
		return suite;
	}

	public TestClassLoading(String entityName) {
		super(entityName);
	}

	@Override
	protected void runTest() throws Throwable {
		entity.getJavaClass();
	}

}
