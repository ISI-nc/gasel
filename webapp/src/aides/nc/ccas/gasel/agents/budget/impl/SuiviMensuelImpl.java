package nc.ccas.gasel.agents.budget.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.ResumeBudgetaire;
import nc.ccas.gasel.agents.budget.SuiviMensuel;
import nc.ccas.gasel.model.budget.BudgetAnnuel;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.cayenne.DataRow;
import org.apache.cayenne.query.SQLTemplate;

public class SuiviMensuelImpl implements SuiviMensuel {

	private static final DateFormat FORMAT = new SimpleDateFormat("MMMMM yyyy");

	private static SQLTemplate QUERY = new SQLTemplate(
			BudgetAnnuel.class,
			"SELECT i.id, i.libelle,\n"
					+ "	      bim.montant AS budget,\n"
					+ "       virement_budget_imp_mensuel(i.id, $annee, $mois) AS virements,\n"
					+ "       montant_inutilise AS bon_inutilise,\n"
					+ "       montant_bons AS total_bons,\n"
					+ "       montant_engage AS total_engage\n"
					+ "\n"
					+ "  FROM budget_imp_mensuel bim\n"
					+ "  JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)\n"
					+ "  JOIN budget_annuel ba ON (bia.parent_id = ba.id)\n"
					+ "  JOIN imputation i ON (bia.imputation_id = i.id)\n"
					+ "  LEFT JOIN bim_montants bimm ON  (bimm.id = bim.id)\n"
					+ "\n" //
					+ " WHERE ba.annee = $annee\n" //
					+ "   AND bim.mois = $mois - 1\n" //
					+ "   AND i.id IN $imp\n" //
					+ " ORDER BY i.libelle" //
	);

	public ResumeBudgetaire getHeader(Date mois) {
		return ResumeBudgetaire.header(FORMAT.format(mois));
	}

	public List<ResumeBudgetaire> getSuivi(BasePageSql sql, Date mois,
			Collection<Imputation> imputations) {
		// Requête
		List<DataRow> rows = sql.query().rows(QUERY, sql.params() //
				.set("imp", imputations) //
				.set("annee", DateUtils.get(mois, Calendar.YEAR)) //
				.set("mois", DateUtils.get(mois, Calendar.MONTH) + 1) //
				);

		// Transformation en résumés
		List<ResumeBudgetaire> retval = new ArrayList<ResumeBudgetaire>(rows
				.size());
		for (DataRow row : rows) {
			retval.add(ResumeBudgetaire.line(row));
		}

		return retval;
	}

}
