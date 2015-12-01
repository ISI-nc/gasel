package nc.ccas.gasel.agents.budget;

import static com.asystan.common.AutoBox.valueOf;
import static nc.ccas.gasel.utils.budget.BudgetUtils.doubleValue;
import static nc.ccas.gasel.utils.budget.BudgetUtils.pourcent;

import java.io.Serializable;

import nc.ccas.gasel.model.budget.Budget;
import nc.ccas.gasel.model.vues.AideResumeMontants;
import nc.ccas.gasel.reports.Cumulable;
import nc.ccas.gasel.utils.budget.BudgetUtils;

import org.apache.cayenne.DataRow;

public class ResumeBudgetaire implements Serializable,
		Cumulable<ResumeBudgetaire> {
	private static final long serialVersionUID = -5395852398729971361L;

	public static ResumeBudgetaire header(String libelle) {
		ResumeBudgetaire resume = new ResumeBudgetaire();
		resume.setHighlight(true);
		resume.setLibelle(libelle);
		return resume;
	}

	public static ResumeBudgetaire line(DataRow row) {
		ResumeBudgetaire resume = new ResumeBudgetaire();

		resume.setHighlight(false);
		resume.setLibelle((String) row.get("libelle"));

		resume.setBudget(doubleValue(row, "budget"));
		resume.setVirements(doubleValue(row, "virements"));
		resume.setInutilise(doubleValue(row, "bon_inutilise"));
		resume.setTotalBons(doubleValue(row, "total_bons"));
		resume.setEngage(doubleValue(row, "total_engage"));

		return resume;
	}

	public static ResumeBudgetaire fromBudget(Budget budget) {
		double bonInutilise = 0;
		double totalBons = 0;
		double totalEngage = 0;
		for (AideResumeMontants montant : budget.getAideMontants()) {
			bonInutilise += montant.getMontantInutilise();
			totalBons += montant.getMontantBons();
			totalEngage += montant.getMontantEngage();
		}

		ResumeBudgetaire resume = new ResumeBudgetaire();
		resume.setLibelle(budget.getLibelle());
		resume.setBudget(budget.getMontant());
		resume.setVirements(budget.getVirements());
		resume.setInutilise(bonInutilise);
		resume.setTotalBons(totalBons);
		resume.setEngage(totalEngage);
		return resume;
	}

	private String libelle;

	private Double budget;

	private Double virements;

	private Double inutilise;

	private Double totalBons;

	private Double engage;

	private boolean highlight = false;

	// ------------------------------------------------------------------

	public void cumule(ResumeBudgetaire other) {
		budget = BudgetUtils.cumule(budget, other.budget);
		virements = BudgetUtils.cumule(virements, other.virements);
		inutilise = BudgetUtils.cumule(inutilise, other.inutilise);
		totalBons = BudgetUtils.cumule(totalBons, other.totalBons);
		engage = BudgetUtils.cumule(engage, other.engage);
	}

	// ------------------------------------------------------------------

	public Double getBonsPourcent() {
		if (budget == null)
			return null;
		return pourcent(getTotalBons(), getBudgetTotal());
	}

	public Double getEngagePourcent() {
		if (budget == null)
			return null;
		return pourcent(getEngage(), getBudgetTotal());
	}

	private double getRefUtilise() {
		return valueOf(totalBons) - valueOf(inutilise);
	}

	private double getRefEngage() {
		return valueOf(engage) - valueOf(inutilise);
	}

	public Double getReste() {
		if (budget == null)
			return null;
		return getBudgetTotal() - getRefUtilise();
	}

	public Double getResteApresEngage() {
		if (budget == null)
			return null;
		return getBudgetTotal() - getRefEngage();
	}

	public boolean isTropUtilise() {
		return getRefUtilise() > getBudgetTotal();
	}

	public boolean isTropEngage() {
		return getRefEngage() > getBudgetTotal();
	}

	public double getBudgetTotal() {
		return valueOf(budget) + valueOf(virements);
	}

	// ------------------------------------------------------------------

	public Double getBudget() {
		return budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public Double getVirements() {
		return virements;
	}

	public void setVirements(Double virements) {
		this.virements = virements;
	}

	public Double getInutilise() {
		return inutilise;
	}

	public void setInutilise(Double inutilise) {
		this.inutilise = inutilise;
	}

	public Double getTotalBons() {
		return totalBons;
	}

	public void setTotalBons(Double utilise) {
		this.totalBons = utilise;
	}

	public Double getEngage() {
		return engage;
	}

	public void setEngage(Double engage) {
		this.engage = engage;
	}

	public boolean isHighlight() {
		return highlight;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

}
