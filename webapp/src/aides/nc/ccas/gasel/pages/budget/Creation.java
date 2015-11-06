package nc.ccas.gasel.pages.budget;

import java.util.Date;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.budget.BudgetAnnuel;
import nc.ccas.gasel.model.budget.BudgetImpAnnuel;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.pages.budget.annuel.EditBudgetImpAnnuel;

public abstract class Creation extends EditPage<BudgetAnnuel> {

	public Creation() {
		super(BudgetAnnuel.class);
	}

	@Override
	protected void prepareNewObject(BudgetAnnuel object) {
		for (Imputation imp : CommonQueries.getAll(getObjectContext(),
				Imputation.class)) {
			BudgetImpAnnuel budImp = createDataObject(BudgetImpAnnuel.class);
			budImp.setImputation(imp);
			budImp.setRepartitionParSecteur(true);
			object.addToBudgetsImputation(budImp);
		}
	}

	public void editerBudgetImpAnnuel(BudgetImpAnnuel budget) {
		EditBudgetImpAnnuel page = (EditBudgetImpAnnuel) getRequestCycle()
				.getPage("budget/annuel/EditBudgetImpAnnuel");
		if (budget != null) {
			page.openWithParent(budget, "parent");
		} else {
			page.activateForParent(getObject(), "parent");
		}
	}

	public Double getBudgetsImputation(Imputation imputation) {
		if (isNew(getObject())) {
			return null;
		}

		for (BudgetImpAnnuel bud : getObject().getBudgetsImputation()) {
			if (bud.getImputation().equals(imputation)) {
				return bud.getMontant();
			}
		}

		return null;
	}

	public boolean getIsValide() {
		return getObject().getValidation() != null;
	}

	public void setIsValide(boolean valide) {
		if (getIsValide() == valide)
			return;
		getObject().setValidation(valide ? new Date() : null);
	}

}
