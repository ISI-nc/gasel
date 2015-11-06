package nc.ccas.gasel.agents.budget.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.DossiersPersonnesMontantParPublic;
import nc.ccas.gasel.agents.budget.result.DossiersPersonnesMontant;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.cayenne.DataRow;
import org.apache.cayenne.query.SQLTemplate;

public class DossiersPersonnesMontantParPublicImpl implements
		DossiersPersonnesMontantParPublic {

	private static final SQLTemplate TEMPLATE = new SQLTemplate(
			Aide.class,
			"" //
					+ "SELECT tp.id AS tp_id,\n"
					+ "       dossiers_imp_public($imp,tp.id,$debut,$fin) AS nb_dossiers,\n"
					+ "       dossiers_personnes_imp_public($imp,tp.id,$debut,$fin) AS nb_personnes,\n"
					+ "       (SELECT SUM(montant_utilise)"
					+ "          FROM aide_montants am"
					+ "          JOIN aide a ON (a.id = am.id)"
					+ "          JOIN nature_aide na ON (na.id = a.nature_id)"
					+ "         WHERE na.imputation_id = $imp"
					+ "           AND am.public_id = tp.id"
					+ "           AND annee_mois BETWEEN $debutYM AND $finYM"
					+ "       ) AS montant" //
					+ "  FROM type_public tp"
					+ " WHERE tp.id IN $publics");

	public Map<TypePublic, DossiersPersonnesMontant> getDossierPersonnesMontantParPublic(
			BasePageSql sql, PeriodeProps periode, Set<TypePublic> publics,
			Imputation imputation) {
		return getDossierPersonnesMontantParPublic(sql, periode, publics, //
				sql.idOf(imputation));
	}

	public Map<TypePublic, DossiersPersonnesMontant> getDossierPersonnesMontantParPublic(
			BasePageSql sql, PeriodeProps periode, Set<TypePublic> publics,
			int imputation) {
		Map<TypePublic, DossiersPersonnesMontant> results = new HashMap<TypePublic, DossiersPersonnesMontant>();

		for (TypePublic publik : publics) {
			results.put(publik, new DossiersPersonnesMontant());
		}

		int debutYM = DateUtils.anneeMois(periode.getPeriodeDebut());
		int finYM = DateUtils.anneeMois(periode.getPeriodeFin());

		List<DataRow> rows = sql.query().rows(TEMPLATE, sql.params() //
				.set("imp", imputation) //
				.setPeriode(periode) //
				.set("debutYM", debutYM) //
				.set("finYM", finYM) //
				.set("publics", publics));

		Map<Integer, TypePublic> tpMap = sql.idMap(publics);
		for (DataRow row : rows) {
			TypePublic pub = tpMap.get(toInt(row, "tp_id"));
			DossiersPersonnesMontant dam = results.get(pub);
			dam.setNbDossiers(toInt(row, "nb_dossiers"));
			dam.setNbPersonnes(toInt(row, "nb_personnes"));
			dam.setMontant(toInt(row, "montant"));
		}

		return results;
	}

	private int toInt(DataRow row, String col) {
		Number number = ((Number) row.get(col));
		if (number == null)
			return 0;
		return number.intValue();
	}

}
