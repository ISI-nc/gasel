package test.aides.budget;

import static nc.ccas.gasel.modelUtils.CommonQueries.findById;
import static nc.ccas.gasel.modelUtils.CommonQueries.getAll;

import java.util.Date;

import junit.framework.TestCase;
import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.BudgetParImpPublicFreq;
import nc.ccas.gasel.agents.budget.MouvementsCredit;
import nc.ccas.gasel.agents.budget.params.BudgetParImpPublicFreqParams;
import nc.ccas.gasel.agents.budget.result.PublicStatutMap;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodePropsBean;

import org.apache.cayenne.access.DataContext;

public class TestCoherenceImpPublicFreqEtMouvements extends TestCase {

	private static BudgetParImpPublicFreq BPIPF = BudgetParImpPublicFreq.INSTANCE;

	private static MouvementsCredit MOUVEMENTS = MouvementsCredit.INSTANCE;

	private BasePageSql sql;

	public void testParPublic() {
		Date debut = DateUtils.debutAnnee();
		Date fin = DateUtils.finAnnee();
		
		MOUVEMENTS.getMouvementsCredit(sql, debut, fin);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		DataContext.bindThreadDataContext(CayenneUtils.createDataContext());
		sql = new BasePageSql(DataContext.getThreadDataContext());
	}

	private void testImputation(int imputationId) {
		Imputation imputation = findById(Imputation.class, imputationId);

		Date debut = DateUtils.debutAnnee();
		Date fin = DateUtils.finAnnee();

		BudgetParImpPublicFreqParams params = new BudgetParImpPublicFreqParams(
				new PeriodePropsBean(debut, fin));
		params.setImputations(imputation);
		params.setStatuts(getAll(StatutAide.class));
		params.setPublics(getAll(TypePublic.class));

		PublicStatutMap<Integer> montantUtilise = BPIPF.getMontantUtilise(sql,
				params);

		int totalBPIPF = 0;
		for (int value : montantUtilise.values()) {
			totalBPIPF += value;
		}
	}

}
