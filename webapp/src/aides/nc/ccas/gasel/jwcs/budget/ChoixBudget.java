package nc.ccas.gasel.jwcs.budget;

import static com.asystan.common.cayenne_new.QueryFactory.createAnd;
import static com.asystan.common.cayenne_new.QueryFactory.createEquals;
import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.model.budget.BudgetImpAnnuel;
import nc.ccas.gasel.model.budget.LigneVirementHelper;

import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.annotations.Parameter;

public abstract class ChoixBudget extends BaseComponent {

	@Parameter(required = true)
	public abstract LigneVirementHelper getBean();

	public BudgetImpAnnuel getBudgetImp() {
		if (getBean().getImputation() == null)
			return null;

		Expression expr = createAnd( //
				createEquals("imputation", getBean().getImputation()), //
				createEquals("parent.annee", getBean().getAnnee()));
		return getSql().query().unique(BudgetImpAnnuel.class, expr);
	}

}
