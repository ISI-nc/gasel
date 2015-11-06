package nc.ccas.gasel.reports;

import java.util.Date;

import nc.ccas.gasel.modelUtils.DateUtils;

public class PeriodePropsBean implements PeriodeProps {

	public static PeriodePropsBean mois(int year, int month) {
		Date[] periode = DateUtils.mois(year, month);
		return new PeriodePropsBean(periode[0], periode[1]);
	}

	private Date debut;

	private Date fin;

	public PeriodePropsBean() {
		// skip
	}

	public PeriodePropsBean(Date debut, Date fin) {
		this.debut = debut;
		this.fin = fin;
	}

	public Date getDefaultPeriodeDebut() {
		return null;
	}

	public Date getDefaultPeriodeFin() {
		return null;
	}

	public Date getPeriodeDebut() {
		return debut;
	}

	public Date getPeriodeFin() {
		return fin;
	}

	public void setPeriodeDebut(Date date) {
		this.debut = date;
	}

	public void setPeriodeFin(Date date) {
		this.fin = date;
	}

}
