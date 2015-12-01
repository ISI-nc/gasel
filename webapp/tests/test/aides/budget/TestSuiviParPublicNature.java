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
import nc.ccas.gasel.agents.budget.SuiviParPublicNature;
import nc.ccas.gasel.agents.budget.result.DossiersPersonnesMontant;
import nc.ccas.gasel.agents.budget.result.PublicNatureMap;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.vues.AideResumeMontants;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.reports.PeriodePropsBean;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;


public class TestSuiviParPublicNature extends TestCase {

	static {
	}

	public void testValeurs() throws Exception {
		BasePageSql sql = BasePageSql.INSTANCE;
		DataContext.bindThreadDataContext(CayenneUtils.createDataContext());

		PeriodePropsBean periode = new PeriodePropsBean(debutAnnee(),
				finAnnee());
		int impId = Imputation.ALIMENTATION;
		Imputation imputation = sql.query().byId(Imputation.class, impId);

		PublicNatureMap<DossiersPersonnesMontant> results = SuiviParPublicNature.INSTANCE
				.getSuiviParPublicNature(periode, imputation.getTypesPublic(),
						imputation);

		assertNotNull(results);

		for (Map.Entry<PublicNatureMap.Key, DossiersPersonnesMontant> entry : results
				.entrySet()) {
			PublicNatureMap.Key key = entry.getKey();
			DossiersPersonnesMontant value = entry.getValue();
			System.out.printf("%-25s %-25s %10d %10d\n", //
					key.getPublic(), //
					key.getNature(), //
					value.getNbDossiers(), //
					value.getMontant());

			// Calcul manuel
			Set<Dossier> dossiers = new HashSet<Dossier>();
			int montant = 0;

			Expression expr = fromString("(db:annee_mois between $debut and $fin)"
					+ " and (public = $public) and (aide.nature = $nature)");
			expr = expr.expWithParameters(sql.params() //
					.set("debut", anneeMois(periode.getPeriodeDebut())) //
					.set("fin", anneeMois(periode.getPeriodeFin())) //
					.set("public", key.getPublic()) //
					.set("nature", key.getNature()));
			for (AideResumeMontants arm : sql.query(AideResumeMontants.class,
					expr, "aide.dossier.dossier")) {
				dossiers.add(arm.getAide().getDossier().getDossier());
				montant += arm.getMontantUtilise();
			}

			// Vérification
			assertEquals("Nb dossiers (" + key + ")", dossiers.size(), value
					.getNbDossiers());
			assertEquals("Montant (" + key + ")", montant, value.getMontant());
		}
	}
}
