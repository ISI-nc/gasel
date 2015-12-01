package nc.ccas.gasel.agents.budget;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.impl.BudgetParImpPublicFreqImpl;
import nc.ccas.gasel.agents.budget.params.BudgetParImpPublicFreqParams;
import nc.ccas.gasel.agents.budget.result.PublicStatutMap;

public interface BudgetParImpPublicFreq {
	
	public static final BudgetParImpPublicFreq INSTANCE = new BudgetParImpPublicFreqImpl();

	public PublicStatutMap<Integer> getDossiers(BasePageSql sql,
			BudgetParImpPublicFreqParams params);

	public PublicStatutMap<Integer> getDossiersAideCreee(BasePageSql sql,
			BudgetParImpPublicFreqParams params);

	public PublicStatutMap<Integer> getDossiersAideSupprimee(BasePageSql sql,
			BudgetParImpPublicFreqParams params);

	public PublicStatutMap<Integer> getPersonnes(BasePageSql sql,
			BudgetParImpPublicFreqParams params);

	public PublicStatutMap<Integer> getMontantUtilise(BasePageSql sql,
			BudgetParImpPublicFreqParams params);

}
