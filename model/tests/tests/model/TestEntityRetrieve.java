package tests.model;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.query.SelectQuery;

public class TestEntityRetrieve extends EntityTestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Recherche des entités");
		for (ObjEntity entity : entities()) {
			suite.addTest(new TestEntityRetrieve(entity.getName()));
		}
		return suite;
	}

	public TestEntityRetrieve(String entityName) {
		super(entityName);
	}

	@Override
	protected void runTest() throws Throwable {
		SelectQuery query = new SelectQuery(entity.getJavaClass());
		query.setFetchLimit(1);
		DataContext.createDataContext().performQuery(query);
	}

}
