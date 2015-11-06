package nc.ccas.gasel.agents.budget.impl;

import static nc.ccas.gasel.agents.budget.ResumeBudgetaire.header;
import static nc.ccas.gasel.modelUtils.DateUtils.get;
import static nc.ccas.gasel.utils.budget.BudgetUtils.intValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.BasePageSort;
import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.French;
import nc.ccas.gasel.SqlParams;
import nc.ccas.gasel.agents.budget.MouvementsCredit;
import nc.ccas.gasel.agents.budget.ResumeBudgetaire;
import nc.ccas.gasel.model.budget.BudgetSecteurAide;
import nc.ccas.gasel.model.budget.BudgetTypePublic;
import nc.ccas.gasel.model.budget.Imputation;

import org.apache.cayenne.DataRow;
import org.apache.cayenne.query.SQLTemplate;

public class MouvementsCreditImpl implements MouvementsCredit {

	private static SQLTemplate BSA_QUERY = new SQLTemplate(
			BudgetSecteurAide.class,
			"SELECT bia.imputation_id AS imp_id, sa.libelle AS libelle,\n"
					+ "    SUM(br.montant) AS budget,\n"
					+ "	   SUM(br.virements) AS virements,\n"
					+ "	   SUM(br.montant_inutilise) AS bon_inutilise,\n"
					+ "	   SUM(br.montant_bons) AS total_bons,\n"
					+ "	   SUM(br.montant_engage) AS total_engage\n"
					+ "\n"
					+ "  FROM budget_secteur_aide bsa\n"
					+ "  JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id AND bim.mois BETWEEN $debut AND $fin)\n"
					+ "  JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id AND bia.repartition_par_secteur = TRUE)\n"
					+ "  JOIN budget_annuel ba ON (ba.id = bia.parent_id AND ba.annee = $annee)\n"
					+ "\n"
					+ "  JOIN secteur_aide sa ON (bsa.secteur_aide_id = sa.id)\n"
					+ "  LEFT JOIN bsa_resume br ON (br.id = bsa.id)\n"
					+ "\n" //
					+ "GROUP BY bia.imputation_id, sa.libelle");

	private static SQLTemplate BTP_QUERY = new SQLTemplate(
			BudgetTypePublic.class,
			"SELECT bia.imputation_id AS imp_id, tp.libelle AS libelle,\n"
					+ "    SUM(br.montant) AS budget,\n"
					+ "	   SUM(br.virements) AS virements,\n"
					+ "	   SUM(br.montant_inutilise) AS bon_inutilise,\n"
					+ "	   SUM(br.montant_bons) AS total_bons,\n"
					+ "	   SUM(br.montant_engage) AS total_engage\n"
					+ "\n"
					+ "  FROM budget_type_public btp\n"
					+ "  JOIN budget_secteur_aide bsa ON (btp.parent_id = bsa.id)\n"
					+ "  JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id AND bim.mois BETWEEN $debut AND $fin)\n"
					+ "  JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id AND bia.repartition_par_secteur = FALSE)\n"
					+ "  JOIN budget_annuel ba ON (ba.id = bia.parent_id AND ba.annee = $annee)\n"
					+ "\n"
					+ "  JOIN type_public tp ON (btp.type_public_id = tp.id)\n"
					+ "  LEFT JOIN btp_resume br ON (br.id = btp.id)\n"
					+ "\n" //
					+ "GROUP BY bia.imputation_id, tp.libelle\n" //
					+ "\n" //
					+ "ORDER BY imp_id, libelle");

	public List<ResumeBudgetaire> getMouvementsCredit(BasePageSql sql,
			Date debut, Date fin) {
		int annee = get(debut, Calendar.YEAR);
		if (annee != get(fin, Calendar.YEAR))
			throw new IllegalArgumentException("Pas la même année (" + debut
					+ " / " + fin + ")");

		return getMouvementsCredit(sql, annee, //
				get(debut, Calendar.MONTH), //
				get(fin, Calendar.MONTH));
	}

	public List<ResumeBudgetaire> getMouvementsCredit(BasePageSql sql,
			int annee, int moisDebut, int moisFin) {
		List<ResumeBudgetaire> tableau = new ArrayList<ResumeBudgetaire>();

		// Récup des imputations
		List<Imputation> imps = sql.query().enumeration(Imputation.class);
		imps = new BasePageSort<Imputation>(imps).byToString().results();

		// Récup des infos
		SqlParams params = sql.params() //
				.set("annee", annee) //
				.set("debut", moisDebut) //
				.set("fin", moisFin) //
		;
		List<DataRow> results = new ArrayList<DataRow>();
		results.addAll(sql.query().rows(BSA_QUERY, params));
		results.addAll(sql.query().rows(BTP_QUERY, params));

		// ----------
		// Synthèse
		tableau = new LinkedList<ResumeBudgetaire>();
		ResumeBudgetaire total = header("Total");

		for (Imputation imp : imps) {
			int impId = sql.idOf(imp);

			// Entete
			tableau.add(header("Caisse " + French.particuleDe(imp.getLibelle())
					+ imp.getLibelle().toLowerCase()));

			// Lignes de détail (BSA, réparti par secteur)
			ResumeBudgetaire sousTotal = header("Sous-total");
			for (DataRow row : results) {
				if (intValue(row, "imp_id") != impId)
					continue;

				ResumeBudgetaire line = ResumeBudgetaire.line(row);
				tableau.add(line);

				// Ajout au sous-total
				sousTotal.cumule(line);
			}

			// Sous-total
			tableau.add(sousTotal);

			// Ajout au total
			total.cumule(sousTotal);
		}

		tableau.add(total);

		return tableau;
	}
}
