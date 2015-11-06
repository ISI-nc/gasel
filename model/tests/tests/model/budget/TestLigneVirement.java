package tests.model.budget;

import static nc.ccas.gasel.model.budget.LigneVirementHelper.BUDGET_ANNUEL;
import static nc.ccas.gasel.model.budget.LigneVirementHelper.BUDGET_IMPUTATION_ANNUEL;
import static nc.ccas.gasel.model.budget.LigneVirementHelper.BUDGET_IMPUTATION_MENSUEL;
import static nc.ccas.gasel.model.budget.LigneVirementHelper.BUDGET_SECTEUR_AIDE;
import static nc.ccas.gasel.model.budget.LigneVirementHelper.BUDGET_TYPE_PUBLIC;
import static nc.ccas.gasel.modelUtils.CommonQueries.findById;
import static org.apache.cayenne.access.DataContext.createDataContext;

import java.util.Calendar;

import junit.framework.TestCase;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.budget.LigneVirement;
import nc.ccas.gasel.model.budget.LigneVirementHelper;
import nc.ccas.gasel.model.budget.SecteurAide;
import nc.ccas.gasel.model.core.enums.TypePublic;

import org.apache.cayenne.access.DataContext;

import tests.model.AllModelTests;

public class TestLigneVirement extends TestCase {

	static {
		AllModelTests.setupDatabase();
	}

	private DataContext dc;

	private LigneVirement lv;

	private LigneVirementHelper helper;

	@Override
	protected void setUp() throws Exception {
		dc = createDataContext();
		lv = (LigneVirement) dc.newObject(LigneVirement.class);
		helper = lv.getHelperSource();
	}

	public void testBudgetAnnuel() {
		helper.setAnnee(2012);

		assertEquals(lv.getSourceType(), (Integer) BUDGET_ANNUEL);
		// assertEquals(lv.getSourceId(), (Integer) 220);
	}

	public void testBudgetImpAnnuel() {
		testBudgetAnnuel();
		helper.setImputation( //
				findById(Imputation.class, Imputation.SOLIDARITE));

		assertEquals(lv.getSourceType(), (Integer) BUDGET_IMPUTATION_ANNUEL);
	}

	public void testBudgetImpMensuel() {
		testBudgetImpAnnuel();
		helper.setMois(Calendar.OCTOBER);

		assertEquals(lv.getSourceType(), (Integer) BUDGET_IMPUTATION_MENSUEL);
	}

	public void testBudgetSecteurAide() {
		testBudgetImpMensuel();
		helper.setSecteur(findById(SecteurAide.class, SecteurAide.LOGEMENT));
		assertEquals(lv.getSourceType(), (Integer) BUDGET_SECTEUR_AIDE);
	}

	public void testBudgetTypePublic() {
		testBudgetSecteurAide();
		TypePublic typePublic = findById(TypePublic.class, 200);
		helper.setPublic(typePublic);
		assertEquals(lv.getSourceType(), (Integer) BUDGET_TYPE_PUBLIC);
		
		assertEquals(typePublic, helper.getPublic());
		System.out.println(helper.budget());
	}

}
