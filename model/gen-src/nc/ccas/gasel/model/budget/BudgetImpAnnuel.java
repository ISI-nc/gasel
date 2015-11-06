package nc.ccas.gasel.model.budget;

import static nc.ccas.gasel.modelUtils.DateUtils.annee;
import static nc.ccas.gasel.modelUtils.DateUtils.anneeMois;

import java.util.List;

import nc.ccas.gasel.SqlParams;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.budget.auto._BudgetImpAnnuel;
import nc.ccas.gasel.model.vues.AideResumeMontants;
import nc.ccas.gasel.model.vues.HasAideMontantsHelper;
import nc.ccas.gasel.modelUtils.MoisLibelle;

public class BudgetImpAnnuel extends _BudgetImpAnnuel implements
		ComplexDeletion, Budget {

	private static final long serialVersionUID = -8202556469169281703L;

	public static int TYPE = 1;

	public BudgetImpMensuel budgetMensuel(MoisLibelle mois) {
		return budgetMensuel(mois.getNumero());
	}

	public BudgetImpMensuel budgetMensuel(int mois) {
		for (BudgetImpMensuel bim : getMensuels()) {
			if ((int) bim.getMois() == mois) {
				return bim;
			}
		}
		return null;
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getMensuels());
	}

	public int getType() {
		return TYPE;
	}

	public String getLibelle() {
		return getImputation().getLibelle();
	}

	public List<BudgetImpMensuel> getChildren() {
		return getMensuels();
	}

	private final HasAideMontantsHelper hamh = new HasAideMontantsHelper(this) {
		@Override
		protected String expression() {
			return "aide.nature.imputation = $imp and"
					+ " db:annee_mois between $debut and $fin";
		}

		@Override
		protected void fillParameters(SqlParams params) {
			params //
					.set("imp", getImputation()) //
					.set("debut", getAnneeMoisMin()) //
					.set("fin", getAnneeMoisMax());
		}
	};

	public int getAnneeMoisMin() {
		return anneeMois(annee(getParent().getAnnee())[0]);
	}

	public int getAnneeMoisMax() {
		return anneeMois(annee(getParent().getAnnee())[1]);
	}

	public List<AideResumeMontants> getAideMontants() {
		return hamh.getAideMontants();
	}

	private final Budget.Helper helper = new Budget.Helper(this);

	public double getVirements() {
		return helper.getMontantVirements();
	}

}
