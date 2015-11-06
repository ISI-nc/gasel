package nc.ccas.gasel.agents.budget.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.ResumeBudgetaire;
import nc.ccas.gasel.agents.budget.SuiviAnnuel;
import nc.ccas.gasel.model.budget.BudgetAnnuel;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.ReportUtils;

import org.apache.cayenne.DataRow;
import org.apache.cayenne.query.SQLTemplate;

public class SuiviAnnuelImpl implements SuiviAnnuel {

	private static SQLTemplate QUERY = new SQLTemplate(
			BudgetAnnuel.class,
			"SELECT i.id, i.libelle,\n"
					+ "	   bia.montant AS budget,\n"
					+ "       virement_budget_imp_annuel(i.id, $annee) AS virements,\n"
					+ "       SUM(montant_inutilise) AS bon_inutilise,\n"
					+ "       SUM(montant_bons) AS total_bons,\n"
					+ "       SUM(montant_engage) AS total_engage\n"
					+ "\n"
					+ "  FROM imputation i\n"
					+ "  JOIN budget_imp_annuel bia ON (bia.imputation_id = i.id)\n"
					+ "  JOIN budget_annuel ba ON (bia.parent_id = ba.id)\n"
					+ "  LEFT JOIN budget_imp_mensuel bim ON (bim.parent_id = bia.id)\n"
					+ "  LEFT JOIN bim_montants bimm ON  (bimm.id = bim.id)\n"
					+ "\n"
					+ " WHERE (ba.annee * 12 + bim.mois+1) BETWEEN $debut AND $fin"
					+ "    OR (ba.annee = $annee AND bim.mois IS NULL)\n"
					+ " \n" //
					+ " GROUP BY i.id, i.libelle, bia.montant\n"
					+ " ORDER BY i.libelle" //
	);

	public List<ResumeBudgetaire> getSuivi(BasePageSql sql, Date debut, Date fin) {
		int annee = DateUtils.get(debut, Calendar.YEAR);
		if (annee != DateUtils.get(fin, Calendar.YEAR))
			throw new IllegalArgumentException("Pas la même année (" + annee
					+ " != " + DateUtils.get(fin, Calendar.YEAR) + ")");

		List<DataRow> rows = sql.query().rows(QUERY, sql.params() //
				.set("annee", annee) //
				.yearMonth("debut", debut) //
				.yearMonth("fin", fin) //
				);

		List<ResumeBudgetaire> retval = new ArrayList<ResumeBudgetaire>(rows
				.size() + 1);
		for (DataRow row : rows) {
			retval.add(ResumeBudgetaire.line(row));
		}

		ResumeBudgetaire cumul = new ResumeBudgetaire();
		ReportUtils.cumule(cumul, retval);
		cumul.setLibelle("Total");
		cumul.setHighlight(true);
		retval.add(cumul);

		return retval;
	}

}
