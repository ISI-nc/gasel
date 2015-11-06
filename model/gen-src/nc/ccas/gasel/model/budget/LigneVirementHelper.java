package nc.ccas.gasel.model.budget;

import static com.asystan.common.cayenne.QueryFactory.createAnd;
import static com.asystan.common.cayenne.QueryFactory.createEquals;
import static nc.ccas.gasel.modelUtils.CommonQueries.unique;
import static org.apache.cayenne.DataObjectUtils.intPKForObject;

import java.io.Serializable;

import nc.ccas.gasel.model.core.enums.TypePublic;

import org.apache.cayenne.ObjectContext;

public class LigneVirementHelper implements Serializable {
	private static final long serialVersionUID = 4165378873725027719L;

	public static final int BUDGET_ANNUEL = 0;

	public static final int BUDGET_IMPUTATION_ANNUEL = 1;

	public static final int BUDGET_IMPUTATION_MENSUEL = 2;

	public static final int BUDGET_SECTEUR_AIDE = 3;

	public static final int BUDGET_TYPE_PUBLIC = 4;

	private Imputation imputation;

	private int annee;

	private Integer mois;

	private SecteurAide secteur;

	private TypePublic _public;

	private final LigneVirement lv;

	private final boolean source;

	public LigneVirementHelper(LigneVirement lv, boolean source) {
		this.lv = lv;
		this.source = source;
	}

	private void update() {
		// Minimum pour avoir une valeur : année
		Integer typeBudget = (annee == 0) ? null : typeBudget();
		Integer idBudget = (annee == 0) ? null : idBudget();

		if (source) {
			lv.setSourceId(idBudget);
			lv.setSourceType(typeBudget);
		} else {
			lv.setDestinationId(idBudget);
			lv.setDestinationType(typeBudget);
		}
	}

	private int typeBudget() {
		if (imputation != null) {
			if (mois != null) {
				if (secteur != null) {
					if (_public != null) {
						return BUDGET_TYPE_PUBLIC;
					}
					return BUDGET_SECTEUR_AIDE;
				}
				return BUDGET_IMPUTATION_MENSUEL;
			}
			return BUDGET_IMPUTATION_ANNUEL;
		}
		return BUDGET_ANNUEL;
	}

	private Integer idBudget() {
		Budget budget = budget();
		if (budget == null)
			return null;
		return intPKForObject(budget);
	}

	public Budget getBudget() {
		return budget();
	}

	public Budget budget() {
		ObjectContext dc = lv.getObjectContext();
		switch (typeBudget()) {
		case BUDGET_ANNUEL:
			return budgetAnnuel(dc);
		case BUDGET_IMPUTATION_ANNUEL:
			return budgetImpAnnuel(dc);
		case BUDGET_IMPUTATION_MENSUEL:
			return budgetImpMensuel(dc);
		case BUDGET_SECTEUR_AIDE:
			return budgetSecteurAide(dc);
		case BUDGET_TYPE_PUBLIC:
			return budgetTypePublic(dc);
		default:
			throw new IllegalStateException();
		}
	}

	public void setBudget(Budget budget) {
		if (budget instanceof BudgetTypePublic) {
			setPublic(((BudgetTypePublic) budget).getTypePublic());
			budget = ((BudgetTypePublic) budget).getParent();
		}
		if (budget instanceof BudgetSecteurAide) {
			setSecteur(((BudgetSecteurAide) budget).getSecteurAide());
			budget = ((BudgetSecteurAide) budget).getParent();
		}
		if (budget instanceof BudgetImpMensuel) {
			setMois(((BudgetImpMensuel) budget).getMois().intValue());
			budget = ((BudgetImpMensuel) budget).getParent();
		}
		if (budget instanceof BudgetImpAnnuel) {
			setImputation(((BudgetImpAnnuel) budget).getImputation());
			budget = ((BudgetImpAnnuel) budget).getParent();
		}
		if (budget instanceof BudgetAnnuel) {
			setAnnee(((BudgetAnnuel) budget).getAnnee());
		}
	}

	private BudgetAnnuel budgetAnnuel(ObjectContext dc) {
		return unique(dc, BudgetAnnuel.class, createEquals("annee", annee));
	}

	private BudgetImpAnnuel budgetImpAnnuel(ObjectContext dc) {
		return unique(dc, BudgetImpAnnuel.class, createAnd( //
				createEquals("parent.annee", annee), //
				createEquals("imputation", imputation)));
	}

	private BudgetImpMensuel budgetImpMensuel(ObjectContext dc) {
		return unique(dc, BudgetImpMensuel.class, createAnd( //
				createEquals("parent.parent.annee", annee), //
				createEquals("parent.imputation", imputation), //
				createEquals("mois", mois-1)));
	}

	private BudgetSecteurAide budgetSecteurAide(ObjectContext dc) {
		return unique(dc, BudgetSecteurAide.class, createAnd( //
				createEquals("parent.parent.parent.annee", annee), //
				createEquals("parent.parent.imputation", imputation), //
				createEquals("parent.mois", mois-1), //
				createEquals("secteurAide", secteur)));
	}

	private BudgetTypePublic budgetTypePublic(ObjectContext dc) {
		return unique(dc, BudgetTypePublic.class, createAnd( //
				createEquals("parent.parent.parent.parent.annee", annee), //
				createEquals("parent.parent.parent.imputation", imputation), //
				createEquals("parent.parent.mois", mois-1), //
				createEquals("parent.secteurAide", secteur), //
				createEquals("typePublic", _public)));
	}

	public TypePublic getPublic() {
		return _public;
	}

	public void setPublic(TypePublic _public) {
		this._public = _public;
		update();
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
		update();
	}

	public Imputation getImputation() {
		return imputation;
	}

	public void setImputation(Imputation imputation) {
		this.imputation = imputation;
		update();
	}

	public Integer getMois() {
		return mois;
	}

	public void setMois(Integer mois) {
		this.mois = mois;
		update();
	}

	public SecteurAide getSecteur() {
		return secteur;
	}

	public void setSecteur(SecteurAide secteur) {
		this.secteur = secteur;
		update();
	}

}