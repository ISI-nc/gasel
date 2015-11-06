package nc.ccas.gasel.model.budget.valid;

import nc.ccas.gasel.model.budget.Budget;
import nc.ccas.gasel.model.budget.BudgetImpAnnuel;
import nc.ccas.gasel.model.budget.BudgetSecteurAide;
import nc.ccas.gasel.model.budget.BudgetTypePublic;
import nc.ccas.gasel.model.budget.LigneVirement;
import nc.ccas.gasel.validator.GaselValidator;

public class LigneVirementValidator extends GaselValidator<LigneVirement> {

	public LigneVirementValidator() {
		super(LigneVirement.class);
	}

	@Override
	protected void validateImpl(LigneVirement object) {
		validateDataObject(object);

		validateBudget("Source", object.getHelperSource().budget());
		validateBudget("Destination", object.getHelperDestination().budget());
	}

	private void validateBudget(String libelle, Budget budget) {
		if (budget == null)
			return;

		if (budget instanceof BudgetTypePublic) {
			BudgetTypePublic btp = (BudgetTypePublic) budget;
			BudgetImpAnnuel bia = btp.getParent().getParent().getParent();
			if (bia.getRepartitionParSecteur()) {
				error(libelle + " invalide : " + bia.getImputation() + "/"
						+ bia.getParent().getAnnee() + " réparti par secteur.");
			}
		} else if (budget instanceof BudgetSecteurAide) {
			BudgetSecteurAide bsa = (BudgetSecteurAide) budget;
			BudgetImpAnnuel bia = bsa.getParent().getParent();
			if (!bia.getRepartitionParSecteur()) {
				error(libelle + " invalide : " + bia.getImputation() + "/"
						+ bia.getParent().getAnnee() + " réparti par public.");
			}
		}
	}

}
