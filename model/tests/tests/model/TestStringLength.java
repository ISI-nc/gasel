package tests.model;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.cayenne.map.ObjAttribute;
import org.apache.cayenne.map.ObjEntity;

import com.asystan.common.StringUtils;

public class TestStringLength extends EntityTestCase {

	public static final Set<String> SKIP = new TreeSet<String>(Arrays.asList(
			"AccompagnementPAPH.commentaire", //
			"AccompagnementPAPH.projet", //

			"ActionCollective.description", //

			"ActionSociale.description", //
			"ActionSociale.projet", //

			"ActivitePersonne.description", //

			"ConstDouble.description", //
			"ConstInteger.description", //
			"ConstString.description", //

			"DemandeAideLogement.evaluationSociale", //
			"DemandeAideLogement.resumeCC", //

			"ModeleDocument.data", //

			"ObservationSiteRHI.observation", //

			"SiteRHI.projet" //
	));

	public static Test suite() {
		TestSuite suite = new TestSuite("TraqueModifs implémenté");
		for (ObjEntity entity : entities()) {
			if (SKIP.contains(entity.getName()))
				continue;
			if (!entity.getDbEntity().getSchema().equals("gasel_v2"))
				continue;

			suite.addTest(new TestStringLength(entity.getName()));
		}
		return suite;
	}

	// --

	public TestStringLength(String entityName) {
		super(entityName);
	}

	@Override
	protected void runTest() throws Throwable {
		Set<String> stringWithoutMaxLength = new TreeSet<String>();
		for (Object o : entity.getAttributes()) {
			ObjAttribute attr = (ObjAttribute) o;

			if (SKIP.contains(entity.getName() + "." + attr.getName()))
				continue;

			if (attr.getJavaClass() != String.class)
				continue;
			if (attr.getDbAttribute().getMaxLength() > 0)
				continue;

			stringWithoutMaxLength.add(attr.getName());
		}

		if (!stringWithoutMaxLength.isEmpty()) {
			System.out.println("- " + entity.getName() + ": "
					+ StringUtils.join(", ", stringWithoutMaxLength));

			AllModelTests.openModelFile(entity);
		}

		assertTrue(StringUtils.join(", ", stringWithoutMaxLength),
				stringWithoutMaxLength.isEmpty());
	}

}
