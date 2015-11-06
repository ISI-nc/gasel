package nc.ccas.gasel.agents.budget;

import java.util.Date;
import java.util.List;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.impl.MouvementsCreditImpl;

public interface MouvementsCredit {

	// TODO Hivemind
	public static final MouvementsCredit INSTANCE = new MouvementsCreditImpl();

	public List<ResumeBudgetaire> getMouvementsCredit(BasePageSql sql,
			Date debut, Date fin);

	public List<ResumeBudgetaire> getMouvementsCredit(BasePageSql sql,
			int annee, int moisDebut, int moisFin);

}
