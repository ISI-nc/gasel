package nc.ccas.gasel.agents.budget.params;

import java.util.Date;

import nc.ccas.gasel.reports.PeriodeProps;

public class BudgetParams {

	private Date debut;

	private Date fin;

	public BudgetParams() {
		// skip
	}

	public BudgetParams(PeriodeProps periode) {
		this.debut = periode.getPeriodeDebut();
		this.fin = periode.getPeriodeFin();
	}

	public Date getDebut() {
		return debut;
	}

	public void setDebut(Date debut) {
		this.debut = debut;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}

}
