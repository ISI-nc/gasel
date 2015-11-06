package nc.ccas.gasel.pages.dossiers.stats;

import java.util.Date;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.core.stats.DossierStats;
import nc.ccas.gasel.reports.PeriodeProps;
import nc.ccas.gasel.stats.TableauStat;

public abstract class StatsSurPeriodePage extends BasePage implements
		PeriodeProps {

	protected abstract TableauStat buildTableau();

	public TableauStat getTableau() {
		TableauStat ts = buildTableau();
		ts.addToSelector(DossierStats.criterePeriode(this));
		return ts;
	}

	public Date getDefaultPeriodeDebut() {
		return dates.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return dates.finAnnee();
	}

}
