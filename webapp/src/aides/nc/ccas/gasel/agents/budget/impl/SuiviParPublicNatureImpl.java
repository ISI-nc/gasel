package nc.ccas.gasel.agents.budget.impl;

import static nc.ccas.gasel.modelUtils.CommonQueries.hollow;
import static nc.ccas.gasel.modelUtils.DateUtils.anneeMois;
import static nc.ccas.gasel.modelUtils.SqlUtils.toInt;

import java.util.Collection;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.SqlParams;
import nc.ccas.gasel.agents.budget.SuiviParPublicNature;
import nc.ccas.gasel.agents.budget.result.DossiersPersonnesMontant;
import nc.ccas.gasel.agents.budget.result.PublicNatureMap;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.cayenne.DataRow;
import org.apache.cayenne.query.SQLTemplate;

public class SuiviParPublicNatureImpl implements SuiviParPublicNature {

	private static SQLTemplate QUERY = new SQLTemplate(
			Aide.class,
			"SELECT tp.id AS tp_id,\n"
					+ "      na.id AS na_id,\n"
					+ "      dossiers_nature_public(na.id, tp.id,$debut,$fin) AS nb_dossiers,\n"
					+ "      (SELECT SUM(montant_utilise)"
					+ "         FROM aide_montants am"
					+ "         JOIN aide a ON (a.id = am.id)"
					+ "        WHERE a.nature_id = na.id"
					+ "          AND am.public_id = tp.id"
					+ "          AND annee_mois BETWEEN $debutYM AND $finYM"
					+ "       ) AS montant\n"
					+ " FROM type_public tp,\n" //
					+ "      nature_aide na\n" //
					+ "WHERE na.imputation_id = $imp\n" //
					+ "  AND tp.id IN $publics\n" //
					+ "GROUP BY tp.id, na.id");

	public PublicNatureMap<DossiersPersonnesMontant> getSuiviParPublicNature(
			PeriodeProps periode, Collection<TypePublic> publics, int imputation) {
		return getSuiviParPublicNature(periode, publics, //
				hollow(Imputation.class, imputation));
	}

	public PublicNatureMap<DossiersPersonnesMontant> getSuiviParPublicNature(
			PeriodeProps periode, Collection<TypePublic> publics,
			Imputation imputation) {
		BasePageSql sql = BasePageSql.INSTANCE;

		PublicNatureMap<DossiersPersonnesMontant> result = new PublicNatureMap<DossiersPersonnesMontant>();

		SqlParams params = sql.params() //
				.setPeriode(periode) //
				.set("debutYM", anneeMois(periode.getPeriodeDebut())) //
				.set("finYM", anneeMois(periode.getPeriodeFin())) //
				.set("imp", imputation) //
				.set("publics", publics);

		for (DataRow row : sql.query().rows(QUERY, params)) {
			int naId = toInt(row.get("na_id"));
			int tpId = toInt(row.get("tp_id"));
			NatureAide natureAide = sql.query().byId(NatureAide.class, naId);
			TypePublic publik = sql.query().byId(TypePublic.class, tpId);

			DossiersPersonnesMontant dpm = new DossiersPersonnesMontant();
			dpm.setNbDossiers(toInt(row.get("nb_dossiers")));
			dpm.setMontant(toInt(row.get("montant")));

			result.put(publik, natureAide, dpm);
		}

		return result;
	}

}
