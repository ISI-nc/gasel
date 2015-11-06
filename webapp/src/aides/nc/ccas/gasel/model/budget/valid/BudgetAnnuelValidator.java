package nc.ccas.gasel.model.budget.valid;

import nc.ccas.gasel.model.budget.BudgetAnnuel;
import nc.ccas.gasel.model.budget.BudgetImpAnnuel;
import nc.ccas.gasel.validator.GaselValidator;

import com.asystan.common.AutoBox;

public class BudgetAnnuelValidator extends GaselValidator<BudgetAnnuel> {

	public BudgetAnnuelValidator() {
		super(BudgetAnnuel.class);
	}

	@Override
	public void validateImpl(BudgetAnnuel object) {
		validateDataObject(object);
		
		if (!checkUnicity(object, "annee", object.getAnnee())) {
			error("Il existe déjà un budget " + object.getAnnee());
		}
		
		if (object.getMontant() != null) {
			int sommeBudImp = 0;
			
			for (BudgetImpAnnuel bia : object.getBudgetsImputation()) {
				sommeBudImp += AutoBox.valueOf(bia.getMontant());
			}
			if (sommeBudImp != object.getMontant()) {
				error("Somme des budgets d'imputation (" + sommeBudImp
						+ ") différente du montant (" + object.getMontant()
						+ ")");
			}
		}
	}

}
