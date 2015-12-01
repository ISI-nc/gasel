package nc.ccas.gasel.pages.budget;

import static com.asystan.common.cayenne.QueryFactory.createAnd;
import static com.asystan.common.cayenne.QueryFactory.createEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.agents.budget.BudgetInformator;
import nc.ccas.gasel.agents.budget.ResumeBudgetaire;
import nc.ccas.gasel.model.budget.Budget;
import nc.ccas.gasel.model.budget.BudgetImpAnnuel;
import nc.ccas.gasel.model.budget.LigneVirement;
import nc.ccas.gasel.model.budget.LigneVirementHelper;
import nc.ccas.gasel.model.vues.AideResumeMontants;

import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;

public abstract class Consultation extends BasePage {

	@Persist("workflow")
	public abstract LigneVirementHelper getBean();

	public abstract void setBean(LigneVirementHelper bean);

	@Persist("workflow")
	public abstract Budget getBudget();

	public abstract void setBudget(Budget budget);

	@Persist("workflow")
	public abstract boolean getShowChildren();

	public abstract void setShowChildren(boolean showChildren);

	@Persist("workflow")
	public abstract boolean getShowChildrenValue();
	
	@Persist("workflow")
	public abstract AideResumeMontants getRefBons();
	
	public abstract void setRefBons(AideResumeMontants montants);

	public void updateBudget() {
		setBudget(getBean().budget());
		setShowChildren(getShowChildrenValue());
		setRefBons(null);
		redirect();
	}

	public void showBons(AideResumeMontants montants) {
		setRefBons(montants);
		redirect();
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		if (getBean() == null) {
			setBean(new LigneVirementHelper(
					createDataObject(LigneVirement.class), true));
		}
	}

	public BudgetInformator getBudgetInformator() {
		return BudgetInformator.INSTANCE;
	}

	public IPropertySelectionModel getAnneeModel() {
		return psm.range(getBudgetInformator().getAnneeMin(),
				getBudgetInformator().getAnneeMax());
	}

	public BudgetImpAnnuel getBudgetImp() {
		if (getBean().getImputation() == null)
			return null;

		Expression expr = createAnd( //
				createEquals("imputation", getBean().getImputation()), //
				createEquals("parent.annee", getBean().getAnnee()));
		return sql.query().unique(BudgetImpAnnuel.class, expr);
	}

	public List<ResumeBudgetaire> getResume() {
		List<ResumeBudgetaire> list = new ArrayList<ResumeBudgetaire>();
		list.add(ResumeBudgetaire.fromBudget(getBudget()));

		if (getShowChildren()) {
			List<Budget> children = new ArrayList<Budget>();
			children.addAll(getBudget().getChildren());
			Collections.sort(children, new Comparator<Budget>() {
				public int compare(Budget o1, Budget o2) {
					return o1.getLibelle().compareTo(o2.getLibelle());
				}
			});

			for (Budget child : children) {
				ResumeBudgetaire resumeBudgetaire = ResumeBudgetaire
						.fromBudget(child);
				resumeBudgetaire.setLibelle("- "
						+ resumeBudgetaire.getLibelle());
				list.add(resumeBudgetaire);
			}
		}

		return list;
	}

}
