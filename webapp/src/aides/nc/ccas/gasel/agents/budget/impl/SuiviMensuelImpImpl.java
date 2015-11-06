package nc.ccas.gasel.agents.budget.impl;

import static nc.ccas.gasel.reports.ReportUtils.asInteger;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.SuiviMensuelImp;
import nc.ccas.gasel.agents.budget.result.SuiviMensuelImpModel;
import nc.ccas.gasel.agents.budget.result.SuiviMensuelImpModel.Element;
import nc.ccas.gasel.model.budget.BudgetImpMensuel;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.sql.QuickAnd;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataRow;

public class SuiviMensuelImpImpl implements SuiviMensuelImp {

	public static void main(String[] args) {
		BasePageSql sql = new BasePageSql(CayenneUtils.createDataContext());
		Imputation imputation = sql.query().byId(Imputation.class,
				Imputation.ALIMENTATION);

		SuiviMensuelImpModel model = SuiviMensuelImp.INSTANCE
				.getSuiviMensuelImp(imputation, Calendar.DECEMBER, 2009, sql);

		for (DataObject key : model.getKeys()) {
			System.out.printf("%-30s %s\n", key, model.getElements().get(key)
					.toTable());
		}
	}

	private static final String QUERY_PAR_SECTEUR = ""
			+ "SELECT bsa.secteur_aide_id AS col,\r\n"
			+ "       SUM(bsa.montant) AS budget_prevu,\r\n"
			+ "       SUM(report) AS report,\r\n"
			+ "       SUM(montant_inutilise) AS inutilise,\r\n"
			+ "       SUM(virements) AS virements,\r\n"
			+ "	   SUM(montant_bons) AS montant_bons\r\n"
			+ "\r\n"
			+ "  FROM budget_secteur_aide bsa\r\n"
			+ "  JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id AND bim.id = $bud)\r\n"
			+ "\r\n"
			+ "  LEFT JOIN bsa_montants  bm ON (bm.id = bsa.id)\r\n"
			+ "  LEFT JOIN bsa_report    br ON (br.id = bsa.id)\r\n"
			+ "  LEFT JOIN bsa_virements bv ON (bv.id = bsa.id)\r\n"
			+ "\r\n" //
			+ " WHERE bsa.secteur_aide_id IN $cols\r\n" //
			+ "\r\n" //
			+ "GROUP BY bsa.secteur_aide_id\r\n";

	private static final String QUERY_PAR_PUBLIC = ""
			+ "SELECT btp.type_public_id AS col,\r\n"
			+ "       SUM(btp.montant) AS budget_prevu,\r\n"
			+ "       SUM(report) AS report,\r\n"
			+ "       SUM(montant_inutilise) AS inutilise,\r\n"
			+ "       SUM(virements) AS virements,\r\n"
			+ "	   SUM(montant_bons) AS montant_bons\r\n"
			+ "\r\n"
			+ "  FROM budget_type_public btp\r\n"
			+ "  JOIN budget_secteur_aide bsa ON (btp.parent_id = bsa.id)\r\n"
			+ "  JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id AND bim.id = $bud)\r\n"
			+ "\r\n"
			+ "  LEFT JOIN btp_montants  bm ON (bm.id = btp.id)\r\n"
			+ "  LEFT JOIN btp_report    br ON (br.id = btp.id)\r\n"
			+ "  LEFT JOIN btp_virements bv ON (bv.id = bsa.id)\r\n"
			+ "\r\n" //
			+ " WHERE btp.type_public_id IN $cols\r\n" //
			+ "\r\n" //
			+ "GROUP BY btp.type_public_id";

	public SuiviMensuelImpModel getSuiviMensuelImp(Imputation imputation,
			int mois, int annee, BasePageSql sql) {

		BudgetImpMensuel budget = sql.query().unique(BudgetImpMensuel.class,
				new QuickAnd() //
						.equals("mois", mois) //
						.equals("parent.imputation", imputation) //
						.equals("parent.parent.annee", annee) //
						.expr());

		String query;
		Set<? extends DataObject> keys;
		if (budget.getParent().getRepartitionParSecteur()) {
			keys = imputation.getSecteursAide();
			query = QUERY_PAR_SECTEUR;
		} else {
			keys = imputation.getTypesPublic();
			query = QUERY_PAR_PUBLIC;
		}

		Map<Integer, DataObject> keyMap = sql.idMap(keys);

		SuiviMensuelImpModel model = new SuiviMensuelImpModel();
		for (DataObject key : keys) {
			model.getElements().put(key, new SuiviMensuelImpModel.Element());
		}

		List<DataRow> results = sql.query().rows(query, sql.params() //
				.set("bud", budget) //
				.set("cols", keys) //
				.set("mois", mois) //
				.set("imp", imputation) //
				);

		for (DataRow row : results) {
			Integer col = asInteger(row.get("col"));
			DataObject key = keyMap.get(col);
			if (key == null) {
				System.out.println("!!! NO MATCH FOR KEY " + col);
				continue;
			}

			Element element = model.getElements().get(key);

			Integer budgetPrevu = asInteger(row.get("budget_prevu"));
			Integer report = asInteger(row.get("report"));
			Integer inutilise = asInteger(row.get("inutilise"));
			Integer virements = asInteger(row.get("virements"));
			Integer montantBons = asInteger(row.get("montant_bons"));

			element.setBudgetPrevu(budgetPrevu);
			element.setReportMoisPrec(report);
			element.setRecupBons(inutilise);
			element.setVirements(virements);
			element.setBonsUOP(montantBons);
		}

		return model;
	}

	public SuiviMensuelImpModel getSuiviMensuelImp(Imputation imputation,
			Date mois, BasePageSql sql) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(mois);
		int moisInt = cal.get(Calendar.MONTH);
		int anneeInt = cal.get(Calendar.YEAR);
		return getSuiviMensuelImp(imputation, moisInt, anneeInt, sql);
	}

}
