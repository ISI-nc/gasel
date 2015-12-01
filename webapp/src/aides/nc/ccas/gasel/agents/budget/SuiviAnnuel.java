package nc.ccas.gasel.agents.budget;

import java.util.Date;
import java.util.List;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.impl.SuiviAnnuelImpl;

public interface SuiviAnnuel {
	
	public static final SuiviAnnuel INSTANCE = new SuiviAnnuelImpl();

	public List<ResumeBudgetaire> getSuivi(BasePageSql sql, Date debut, Date fin);

}
