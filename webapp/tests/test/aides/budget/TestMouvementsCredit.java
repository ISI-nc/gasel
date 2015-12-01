package test.aides.budget;

import java.util.List;

import junit.framework.TestCase;
import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.MouvementsCredit;
import nc.ccas.gasel.agents.budget.ResumeBudgetaire;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.DateUtils;

public class TestMouvementsCredit extends TestCase {

	static {
	}

	public void testMouvementsCredit() throws Exception {
		BasePageSql sql = new BasePageSql(CayenneUtils.createDataContext());
		List<ResumeBudgetaire> mouvementsCredit = MouvementsCredit.INSTANCE.getMouvementsCredit(sql, DateUtils
				.debutMois(), DateUtils.finMois());
		
		for (ResumeBudgetaire res : mouvementsCredit) {
			System.out.println(res.getLibelle());
		}
	}

}
