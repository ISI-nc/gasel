package tests.model;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.map.ObjEntity;

public class TestEntityCreation extends EntityTestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Création des entités");
		for (ObjEntity entity : entities()) {
			suite.addTest(new TestEntityCreation(entity.getName()));
		}
		return suite;
	}

	public TestEntityCreation(String entityName) {
		super(entityName);
	}

	@Override
	protected void runTest() throws Throwable {
		DataContext.createDataContext().newObject(entity.getJavaClass());
	}

}
