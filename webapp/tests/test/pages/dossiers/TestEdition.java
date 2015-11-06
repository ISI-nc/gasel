package test.pages.dossiers;

import nc.ccas.gasel.LoginData;
import nc.ccas.gasel.pages.dossiers.Edition;
import nc.ccas.gasel.workflow.Workflow;

import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;

@Test
public class TestEdition extends BaseComponentTestCase {

	public static void main(String[] args) {
		TestEdition test = new TestEdition();
		test.test_SansDossier();
	}

	@Test
	public void test_SansDossier() {
		replay();

		Edition page = newInstance(Edition.class, //
				"workflow", new Workflow("dossiers.Edition", "Dossier"), //
				"login", new LoginData() //
		);
		assertNotNull(page);
		page.activate();
		assertNotNull(page.getObject());
	}

}
