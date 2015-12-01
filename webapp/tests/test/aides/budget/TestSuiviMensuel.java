package test.aides.budget;

import junit.framework.TestCase;
import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.SuiviMensuel;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.DateUtils;


public class TestSuiviMensuel extends TestCase {
	
	static {
	}

	public void testSuivi() throws Exception {
		BasePageSql sql = new BasePageSql(CayenneUtils.createDataContext());
		SuiviMensuel.INSTANCE.getSuivi(sql, DateUtils.debutMois(), //
				sql.query().all(Imputation.class));
	}

}
