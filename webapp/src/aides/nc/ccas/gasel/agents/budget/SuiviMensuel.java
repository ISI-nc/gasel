package nc.ccas.gasel.agents.budget;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.ResumeBudgetaire;
import nc.ccas.gasel.agents.budget.impl.SuiviMensuelImpl;
import nc.ccas.gasel.model.budget.Imputation;

/**
 * Agent d'information pour le suivi mensuel du budget.
 * 
 * @author ISI.NC - Mikaël Cluseau
 * 
 */
public interface SuiviMensuel {

	// FIXME À mettre en service HiveMind
	public static final SuiviMensuel INSTANCE = new SuiviMensuelImpl();

	/**
	 * Renvoie une ligne d'entête pour le <code>mois</code> donné.
	 */
	public ResumeBudgetaire getHeader(Date mois);

	/**
	 * Renvoie le suivi budgetaire pour le <code>mois</code> et les
	 * <code>imputations</code> données.
	 */
	public List<ResumeBudgetaire> getSuivi(BasePageSql sql, Date mois,
			Collection<Imputation> imputations);

}
