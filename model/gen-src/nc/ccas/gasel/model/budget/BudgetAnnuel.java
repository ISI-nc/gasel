package nc.ccas.gasel.model.budget;

import java.util.Date;
import java.util.List;

import nc.ccas.gasel.SqlParams;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.budget.auto._BudgetAnnuel;
import nc.ccas.gasel.model.vues.AideResumeMontants;
import nc.ccas.gasel.model.vues.HasAideMontantsHelper;
import nc.ccas.gasel.modelUtils.DateUtils;

public class BudgetAnnuel extends _BudgetAnnuel implements ComplexDeletion,
		Budget {
	private static final long serialVersionUID = -533535888940232463L;

	public static int TYPE = 0;

	public void prepareForDeletion() {
		DeletionUtils.delete(getBudgetsImputation());
	}

	public int getType() {
		return TYPE;
	}

	public String getLibelle() {
		return String.valueOf(getAnnee());
	}

	public Budget getParent() {
		return null;
	}

	public List<BudgetImpAnnuel> getChildren() {
		return getBudgetsImputation();
	}

	private final HasAideMontantsHelper hamh = new HasAideMontantsHelper(this) {
		@Override
		protected String expression() {
			return "db:annee_mois between $debut and $fin";
		}

		@Override
		protected void fillParameters(SqlParams params) {
			Date[] annee = DateUtils.annee(getAnnee());
			params //
					.set("debut", DateUtils.anneeMois(annee[0])) //
					.set("fin", DateUtils.anneeMois(annee[1]));
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
