package nc.ccas.gasel.agents.budget;

import java.util.Date;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.impl.SuiviMensuelImpImpl;
import nc.ccas.gasel.agents.budget.result.SuiviMensuelImpModel;
import nc.ccas.gasel.model.budget.Imputation;

public interface SuiviMensuelImp {

	public static final SuiviMensuelImp INSTANCE = new SuiviMensuelImpImpl();

	public SuiviMensuelImpModel getSuiviMensuelImp(Imputation imputation,
			int mois, int annee, BasePageSql sql);

	public SuiviMensuelImpModel getSuiviMensuelImp(Imputation imputation,
			Date mois, BasePageSql sql);

}
