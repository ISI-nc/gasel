package nc.ccas.gasel.model.budget;

import static nc.ccas.gasel.modelUtils.DateUtils.anneeMois;

import java.util.List;

import nc.ccas.gasel.SqlParams;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.budget.auto._BudgetImpMensuel;
import nc.ccas.gasel.model.vues.AideResumeMontants;
import nc.ccas.gasel.model.vues.HasAideMontantsHelper;

public class BudgetImpMensuel extends _BudgetImpMensuel implements
		ComplexDeletion, Budget {
	private static final long serialVersionUID = 3672780682570056066L;

	public static int TYPE = 2;

	public BudgetSecteurAide budgetSecteurAide(SecteurAide secteurAide) {
		for (BudgetSecteurAide bsa : getParSecteur()) {
			if ((bsa.getSecteurAide()).equals(secteurAide)) {
				return bsa;
			}
		}
		return null;
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getParSecteur());
	}

	public int getType() {
		return TYPE;
	}

	public String getLibelle() {
		return String.format("%02d", getMois() + 1);
	}

	public List<BudgetSecteurAide> getChildren() {
		return getParSecteur();
	}

	private final HasAideMontantsHelper hamh = new HasAideMontantsHelper(this) {
		@Override
		protected String expression() {
			return "aide.nature.imputation = $imp and db:annee_mois = $anneeMois";
		}

		@Override
		protected void fillParameters(SqlParams params) {
			params //
					.set("imp", getParent().getImputation()) //
					.set("anneeMois", getAnneeMois());
		}
	};

	public int getAnneeMois() {
		return anneeMois(getParent().getParent().getAnnee(), getMois());
	}

	public List<AideResumeMontants> getAideMontants() {
		return hamh.getAideMontants();
	}

	private final Budget.Helper helper = new Budget.Helper(this);

	public double getVirements() {
		return helper.getMontantVirements();
	}

}
