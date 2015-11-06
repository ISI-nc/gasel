package nc.ccas.gasel.model.budget;

import java.util.Collections;
import java.util.List;

import nc.ccas.gasel.SqlParams;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.budget.auto._BudgetSecteurAide;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.model.vues.AideResumeMontants;
import nc.ccas.gasel.model.vues.HasAideMontantsHelper;

public class BudgetSecteurAide extends _BudgetSecteurAide implements
		ComplexDeletion, Budget {
	private static final long serialVersionUID = 5331321163959873952L;

	public static int TYPE = 3;

	public BudgetTypePublic budgetPublic(TypePublic tp) {
		for (BudgetTypePublic btp : getParPublic()) {
			if ((btp.getTypePublic()).equals(tp)) {
				return btp;
			}
		}
		return null;
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getParPublic());
	}

	public int getType() {
		return TYPE;
	}

	public String getLibelle() {
		return getSecteurAide().getLibelle();
	}

	public List<BudgetTypePublic> getChildren() {
		if (getParent().getParent().getRepartitionParSecteur()) {
			return Collections.emptyList();
		}
		return getParPublic();
	}

	private final HasAideMontantsHelper hamh = new HasAideMontantsHelper(this) {
		@Override
		protected String expression() {
			return "aide.nature.imputation = $imp"
					+ " and aide.nature.parent = $secteur"
					+ " and db:annee_mois = $anneeMois";
		}

		@Override
		protected void fillParameters(SqlParams params) {
			params //
					.set("imp", getParent().getParent().getImputation()) //
					.set("secteur", getSecteurAide()) //
					.set("anneeMois", getParent().getAnneeMois());
		}
	};

	public List<AideResumeMontants> getAideMontants() {
		return hamh.getAideMontants();
	}

	private final Budget.Helper helper = new Budget.Helper(this);

	public double getVirements() {
		return helper.getMontantVirements();
	}

}
