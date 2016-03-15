package nc.ccas.gasel.pages.stats;

import java.util.Date;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.reports.PeriodeProps;

public abstract class Actualite extends BasePage implements PeriodeProps {

	public int getDefaultAnnee() {
		return dates.getAnneeEnCours() - 1;
	}

	public Date getDefaultPeriodeDebut() {
		return dates.debutAnnee(getDefaultAnnee());
	}

	public Date getDefaultPeriodeFin() {
		return dates.finAnnee(getDefaultAnnee());
	}

	public String getReportLink() {
		return "/reports/actualite?debut=" + getPeriodeDebut().getTime()
				+ "&fin=" + getPeriodeFin().getTime();
	}

}
