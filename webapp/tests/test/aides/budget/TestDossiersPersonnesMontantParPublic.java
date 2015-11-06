package test.aides.budget;

import static nc.ccas.gasel.modelUtils.DateUtils.anneeMois;
import static nc.ccas.gasel.modelUtils.DateUtils.debutAnnee;
import static nc.ccas.gasel.modelUtils.DateUtils.finAnnee;
import static org.apache.cayenne.exp.Expression.fromString;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.DossiersPersonnesMontantParPublic;
import nc.ccas.gasel.agents.budget.result.DossiersPersonnesMontant;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.model.vues.AideResumeMontants;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.reports.PeriodePropsBean;

import org.apache.cayenne.exp.Expression;


public class TestDossiersPersonnesMontantParPublic extends TestCase {

	static {
	}

	public void testValeurs() throws Exception {
		BasePageSql sql = new BasePageSql(CayenneUtils.createDataContext());

		PeriodePropsBean periode = new PeriodePropsBean(debutAnnee(),
				finAnnee());
		int imputation = Imputation.ALIMENTATION;

		Map<TypePublic, DossiersPersonnesMontant> results = DossiersPersonnesMontantParPublic.INSTANCE
				.getDossierPersonnesMontantParPublic(sql, periode, sql.query()
						.all(TypePublic.class), imputation);

		assertNotNull(results);

		for (Map.Entry<TypePublic, DossiersPersonnesMontant> entry : results
				.entrySet()) {
			TypePublic key = entry.getKey();
			DossiersPersonnesMontant value = entry.getValue();
			System.out.printf("%-30s %10d %10d %10d\n", //
					key.getLibelle(), //
					value.getNbDossiers(), //
					value.getNbPersonnes(), //
					value.getMontant());

			// Calcul manuel
			Set<Dossier> dossiers = new HashSet<Dossier>();
			Set<Personne> personnes = new HashSet<Personne>();
			int montant = 0;

			Expression expr = fromString("(db:annee_mois between $debut and $fin)"
					+ " and (public = $public) and (aide.nature.imputation = $imputation)");
			expr = expr.expWithParameters(sql.params() //
					.set("debut", anneeMois(periode.getPeriodeDebut())) //
					.set("fin", anneeMois(periode.getPeriodeFin())) //
					.set("public", key) //
					.set("imputation", imputation));
			for (AideResumeMontants arm : sql.query(
					AideResumeMontants.class,
					expr, //
					"aide.dossier.dossier." + Dossier.CHEF_FAMILLE_PROPERTY,
					"aide.dossier.dossier." + Dossier.CONJOINT_PROPERTY,
					"aide.dossier.dossier."
							+ Dossier.PERSONNES_ACHARGE_PROPERTY,
					"aide.dossier.dossier."
							+ Dossier.PERSONNES_NON_ACHARGE_PROPERTY)) {
				dossiers.add(arm.getAide().getDossier().getDossier());
				personnes.addAll(arm.getAide().getDossier().getDossier()
						.getPersonnes());
				montant += arm.getMontantUtilise();
			}

			// Vérification
			assertEquals("Nb dossiers (" + key + ")", dossiers.size(), value
					.getNbDossiers());
			assertEquals("Nb personnes (" + key + ")", personnes.size(), value
					.getNbPersonnes());
			assertEquals("Montant (" + key + ")", montant, value.getMontant());
		}
	}
}
