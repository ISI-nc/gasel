package nc.ccas.gasel.model.budget;

import java.util.Collections;
import java.util.List;

import nc.ccas.gasel.SqlParams;
import nc.ccas.gasel.model.budget.auto._BudgetTypePublic;
import nc.ccas.gasel.model.vues.AideResumeMontants;
import nc.ccas.gasel.model.vues.HasAideMontantsHelper;

public class BudgetTypePublic extends _BudgetTypePublic implements Budget {

	private static final long serialVersionUID = -4590101820154958427L;

	public static int TYPE = 4;

	public int getType() {
		return TYPE;
	}

	public String getLibelle() {
		return getTypePublic().getLibelle();
	}

	public List<? extends Budget> getChildren() {
		return Collections.emptyList();
	}

	private final HasAideMontantsHelper hamh = new HasAideMontantsHelper(this) {
		@Override
		protected String expression() {
			return "aide.nature.imputation = $imp"
					+ " and aide.nature.parent = $secteur"
					+ " and aide.public = $public"
					+ " and db:annee_mois = $anneeMois";
		}

		@Override
		protected void fillParameters(SqlParams params) {
			BudgetImpAnnuel bia = getParent().getParent().getParent();
			params //
					.set("imp", bia.getImputation()) //
					.set("secteur", getParent().getSecteurAide()) //
					.set("public", getTypePublic()) //
					.set("anneeMois", getParent().getParent().getAnneeMois());
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
