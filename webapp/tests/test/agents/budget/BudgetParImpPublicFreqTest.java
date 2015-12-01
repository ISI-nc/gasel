package test.agents.budget;

import junit.framework.TestCase;
import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.impl.BudgetParImpPublicFreqImpl;
import nc.ccas.gasel.agents.budget.params.BudgetParImpPublicFreqParams;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.cayenne.access.DataContext;


public class BudgetParImpPublicFreqTest extends TestCase {
	
	static {
	}

	private BudgetParImpPublicFreqImpl agent;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		agent = new BudgetParImpPublicFreqImpl();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		agent = null;
	}
	
	public void testGetMontantUtilise() throws Exception {
		DataContext context = CayenneUtils.createDataContext();
		BasePageSql sql = new BasePageSql(context);
		
		BudgetParImpPublicFreqParams params = new BudgetParImpPublicFreqParams();
		
		params.setDebut(DateUtils.debutAnnee());
		params.setFin(DateUtils.finAnnee());
		
		Imputation impAlim = sql.query().byId(Imputation.class, Imputation.ALIMENTATION);
		params.setImputations(impAlim);
		params.setStatuts(StatutAide.OCCASIONNELLE, StatutAide.IMMEDIATE);
		params.setPublics(impAlim.getTypesPublic());
		
		agent.getMontantUtilise(sql, params);
	}

}
