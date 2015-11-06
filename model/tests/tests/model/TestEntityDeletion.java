package tests.model;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import nc.ccas.gasel.model.ComplexDeletion;

import org.apache.cayenne.Persistent;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;

public class TestEntityDeletion extends EntityTestCase {

	public static final Set<String> SKIP = new TreeSet<String>(Arrays.asList(
			"CategorieCharge", "CategorieRessource", "NatureAide",
			"SecteurAide"));

	public static Test suite() {
		TestSuite suite = new TestSuite("ComplexDeletion implémenté");
		for (ObjEntity entity : entities()) {
			if (SKIP.contains(entity.getName()))
				continue;
			if (!entity.getDbEntity().getSchema().equals("gasel_v2"))
				continue;
			if (entity.getDataMap().getDefaultPackage().endsWith(".enums"))
				continue;

			boolean hasToMany = false;
			for (Object o : entity.getRelationships()) {
				if (((ObjRelationship) o).isToMany()) {
					hasToMany = true;
					break;
				}
			}
			if (!hasToMany)
				continue;
			suite.addTest(new TestEntityDeletion(entity.getName()));
		}
		return suite;
	}

	public TestEntityDeletion(String entityName) {
		super(entityName);
	}

	@Override
	protected void runTest() throws Throwable {
		Persistent object = DataContext.createDataContext().newObject(
				entity.getJavaClass().asSubclass(Persistent.class));
		assertTrue(toManyRelNames(), object instanceof ComplexDeletion);
	}

	private String toManyRelNames() {
		Set<String> rels = new TreeSet<String>();

		for (Object o : entity.getRelationships()) {
			if (((ObjRelationship) o).isToMany()) {
				rels.add(((ObjRelationship) o).getName());
			}
		}

		return rels.toString();
	}

}
