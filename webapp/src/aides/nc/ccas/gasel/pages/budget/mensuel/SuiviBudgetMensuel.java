package nc.ccas.gasel.pages.budget.mensuel;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.ValueFactory;
import nc.ccas.gasel.agents.budget.ResumeBudgetaire;
import nc.ccas.gasel.agents.budget.SuiviMensuel;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;

public abstract class SuiviBudgetMensuel extends BasePage implements
		PeriodeProps {

	public List<ResumeBudgetaire> getTableau() {
		List<ResumeBudgetaire> tableau = new LinkedList<ResumeBudgetaire>();

		Date debut = getPeriodeDebut();
		Date fin = getPeriodeFin();

		tableau = new ArrayList<ResumeBudgetaire>();
		for (Date mois = debut; mois.before(fin); mois = dates.nextMonth(mois)) {
			tableau.add(SuiviMensuel.INSTANCE.getHeader(mois));
			tableau.addAll(stats(mois));
		}

		return tableau;
	}

	public Date getDefaultPeriodeDebut() {
		return DateUtils.debutMois();
	}

	public Date getDefaultPeriodeFin() {
		return DateUtils.finMois();
	}

	// ------------------------------------------------------------------
	// Cache
	//

	private Set<Imputation> imputations() {

		return getCache().get(new ValueFactory<Set<Imputation>>() {

			public Set<Imputation> buildValue() {
				return sql.query().all(Imputation.class);
			};

		}, "imputations");
	}

	public List<ResumeBudgetaire> stats(final Date mois) {

		return getCache().get(new ValueFactory<List<ResumeBudgetaire>>() {

			public List<ResumeBudgetaire> buildValue() {
				return SuiviMensuel.INSTANCE.getSuivi(sql, mois, imputations());
			}

		}, "stats", mois);
	}

}
