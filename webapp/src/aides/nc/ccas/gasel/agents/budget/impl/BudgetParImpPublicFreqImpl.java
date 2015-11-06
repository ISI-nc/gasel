package nc.ccas.gasel.agents.budget.impl;

import static nc.ccas.gasel.modelUtils.DateUtils.anneeMois;

import java.util.List;
import java.util.Map;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.BudgetParImpPublicFreq;
import nc.ccas.gasel.agents.budget.params.BudgetParImpPublicFreqParams;
import nc.ccas.gasel.agents.budget.result.PublicStatutMap;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.cayenne.DataRow;
import org.apache.cayenne.query.SQLTemplate;

public class BudgetParImpPublicFreqImpl implements BudgetParImpPublicFreq {

	private static final SQLTemplate QUERY_DOSSIERS = new SQLTemplate(
			Aide.class,
			"SELECT fa.id AS freq_id, tp.id AS tp_id,\n"
					+ "    SUM(dossiers_imp_freq_public(imp.id,fa.id,tp.id,$debut,$fin))::integer AS value\n"
					+ "FROM statut_aide fa,\n"
					+ "    type_public tp,\n" //
					+ "    imputation imp\n"
					+ "WHERE fa.id IN $statuts AND tp.id IN $types_public\n"
					+ "  AND imp.id IN $imp_id" //
					+ " GROUP BY fa.id, tp.id");

	private static final SQLTemplate QUERY_DOSSIERS_AIDE_CREEE = new SQLTemplate(
			Aide.class,
			"SELECT fa.id AS freq_id, tp.id AS tp_id,\n"
					+ "    SUM(dossiers_aide_cree_imp_freq_public(imp.id,fa.id,tp.id,$debut,$fin))::integer AS value\n"
					+ "FROM statut_aide fa,\n"
					+ "    type_public tp,\n" //
					+ "    imputation imp\n"
					+ "WHERE fa.id IN $statuts AND tp.id IN $types_public\n"
					+ "  AND imp.id IN $imp_id" //
					+ " GROUP BY fa.id, tp.id");

	private static final SQLTemplate QUERY_DOSSIERS_AIDE_SUPPRIMEE = new SQLTemplate(
			Aide.class,
			"SELECT fa.id AS freq_id, tp.id AS tp_id,\n"
					+ "    SUM(dossiers_aide_suppr_imp_freq_public(imp.id,fa.id,tp.id,$debut,$fin))::integer AS value\n"
					+ "FROM statut_aide fa,\n"
					+ "    type_public tp,\n" //
					+ "    imputation imp\n"
					+ "WHERE fa.id IN $statuts AND tp.id IN $types_public\n"
					+ "  AND imp.id IN $imp_id" //
					+ " GROUP BY fa.id, tp.id");

	private static final SQLTemplate QUERY_PERSONNES = new SQLTemplate(
			Aide.class,
			"" //
					+ "SELECT fa.id AS freq_id, tp.id AS tp_id,\n"
					+ "       SUM(dossiers_personnes_imp_freq_public(imp.id,fa.id,tp.id,$debut,$fin))::integer\n"
					+ "       AS value\n"
					+ "FROM statut_aide fa,\n"
					+ "    type_public tp,\n" //
					+ "    imputation imp\n"
					+ "WHERE fa.id IN $statuts"
					+ "  AND tp.id IN $types_public\n"
					+ "  AND imp.id IN $imp_id" //
					+ " GROUP BY fa.id, tp.id");

	private static final SQLTemplate QUERY_MONTANT_UTILISE = new SQLTemplate(
			Aide.class,
			"SELECT fa.id AS freq_id, tp.id AS tp_id, coalesce(SUM(am.montant_utilise),0)::integer AS value\n"
					+ "  FROM aide a\n"
					+ "  JOIN statut_aide fa ON (a.statut_id = fa.id)\n"
					+ "  JOIN type_public tp ON (a.public_id = tp.id)\n"
					+ "  JOIN nature_aide na ON (a.nature_id = na.id)\n"
					+ "  JOIN imputation imp ON (na.imputation_id = imp.id)\n"
					+ "  JOIN aide_montants am ON (a.id = am.id\n"
					+ "                                 AND am.annee_mois BETWEEN $debutYM AND $finYM)\n"
					+ " WHERE tp.id IN $types_public\n"
					+ "   AND fa.id IN $statuts\n"
					+ "   AND imp.id IN $imp_id\n" //
					+ " GROUP BY fa.id, tp.id");

	public PublicStatutMap<Integer> getDossiers(BasePageSql sql,
			BudgetParImpPublicFreqParams params) {
		return doQuery(QUERY_DOSSIERS, Integer.class, 0, sql, params);
	}

	public PublicStatutMap<Integer> getDossiersAideCreee(BasePageSql sql,
			BudgetParImpPublicFreqParams params) {
		return doQuery(QUERY_DOSSIERS_AIDE_CREEE, Integer.class, 0, sql, params);
	}

	public PublicStatutMap<Integer> getDossiersAideSupprimee(BasePageSql sql,
			BudgetParImpPublicFreqParams params) {
		return doQuery(QUERY_DOSSIERS_AIDE_SUPPRIMEE, Integer.class, 0, sql,
				params);
	}

	public PublicStatutMap<Integer> getMontantUtilise(BasePageSql sql,
			BudgetParImpPublicFreqParams params) {
		return doQuery(QUERY_MONTANT_UTILISE, Integer.class, 0, sql, params);
	}

	public PublicStatutMap<Integer> getPersonnes(BasePageSql sql,
			BudgetParImpPublicFreqParams params) {
		return doQuery(QUERY_PERSONNES, Integer.class, 0, sql, params);
	}

	private <V> PublicStatutMap<V> doQuery(SQLTemplate template,
			Class<V> valueClass, V defaultValue, BasePageSql sql,
			BudgetParImpPublicFreqParams params) {
		List<DataRow> rows = sql.query().rows(template, sql.params() //
				.set("debut", params.getDebut()) // 
				.set("fin", params.getFin()) //
				.set("debutYM", anneeMois(params.getDebut())) // 
				.set("finYM", anneeMois(params.getFin())) //
				.set("split", DateUtils.finMois()) //
				.set("imp_id", params.getImputations()) //
				.set("statuts", params.getStatuts()) //
				.set("types_public", params.getPublics()));

		Map<Integer, TypePublic> publicsById = sql.pkMap(params.getPublics());
		Map<Integer, StatutAide> freqsById = sql.pkMap(params
				.getStatuts());

		PublicStatutMap<V> result = new PublicStatutMap<V>(defaultValue);
		for (DataRow row : rows) {
			StatutAide freq = freqsById.get(asInt(row, "freq_id"));
			TypePublic publik = publicsById.get(asInt(row, "tp_id"));
			V value = valueClass.cast(row.get("value"));

			if (freq == null) {
				throw new NullPointerException("StatutAide invalide ("
						+ row.get("freq_id") + "): doit être dans "
						+ freqsById.keySet());
			}

			result.put(publik, freq, value);
		}
		return result;
	}

	private Integer asInt(DataRow row, String column) {
		return ((Number) row.get(column)).intValue();
	}

}
