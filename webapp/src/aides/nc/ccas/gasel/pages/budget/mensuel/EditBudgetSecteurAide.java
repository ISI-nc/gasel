package nc.ccas.gasel.pages.budget.mensuel;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.budget.BudgetSecteurAide;
import nc.ccas.gasel.model.budget.BudgetTypePublic;
import nc.ccas.gasel.model.core.enums.TypePublic;

public abstract class EditBudgetSecteurAide extends EditPage<BudgetSecteurAide> {
	
	public EditBudgetSecteurAide() {
		super(BudgetSecteurAide.class);
	}
	
	public abstract BudgetTypePublic getParPublic();
	
	public abstract TypePublic getTypePublic();

	public abstract BudgetTypePublic getBudgetTypePublic();
	
	public BudgetTypePublic findBudgetTypePublic(TypePublic typeP) {
		for (BudgetTypePublic bud : getObject().getParPublic()) {
			if (typeP.equals(bud.getParent().getParPublic())) {
				return bud;
			}
		}
		return null;
	}
	
	public void editerBudgetTypePublic(TypePublic typeP) {
		BudgetTypePublic budget = findBudgetTypePublic(typeP);
		EditBudgetTypePublic page  = (EditBudgetTypePublic) getRequestCycle()
				.getPage("budget/mensuel/EditBudgetTypePublic");
		page.open(budget);

	}
	
	

}
