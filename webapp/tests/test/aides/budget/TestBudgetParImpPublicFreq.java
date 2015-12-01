package test.aides.budget;

import static com.asystan.common.cayenne_new.QueryFactory.createBetween;
import static com.asystan.common.cayenne_new.QueryFactory.createIn;
import static nc.ccas.gasel.agents.budget.BudgetParImpPublicFreq.INSTANCE;
import static nc.ccas.gasel.model.aides.StatutAide.PLURIMENSUELLE;
import static nc.ccas.gasel.modelUtils.DateUtils.intersection;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.Check;
import nc.ccas.gasel.agents.budget.params.BudgetParImpPublicFreqParams;
import nc.ccas.gasel.agents.budget.result.PublicStatutMap;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.AspectAides;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.model.vues.AideResumeMontants;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodePropsBean;
import nc.ccas.gasel.sql.QuickAnd;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;

public class TestBudgetParImpPublicFreq extends TestCase {

	private Date[] intervalle = DateUtils.mois(2009, Calendar.FEBRUARY);
	
	private Date debut = intervalle[0];

	private Date fin = intervalle[1];

	private BudgetParImpPublicFreqParams params;

	private BasePageSql sql;

	@Override
	public void setUp() {
		params = new BudgetParImpPublicFreqParams(new PeriodePropsBean(debut,
				fin));
		params.setImputations(Imputation.ALIMENTATION);
		params.setStatuts(PLURIMENSUELLE);
		params.setPublics(CommonQueries.getAll(TypePublic.class));

		sql = new BasePageSql(DataContext.getThreadDataContext());
	}

	public void testDossiers() throws Exception {
		PublicStatutMap<Integer> result = INSTANCE.getDossiers(sql, params);
		basicAssertions(result);

		for (Map.Entry<PublicStatutMap.Key, Integer> entry : result.entrySet()) {
			PublicStatutMap.Key key = entry.getKey();

			List<Aide> aides = aides(key, params);

			Set<AspectAides> dossiers = dossiers(aides);
			assertEquals("Nombre (" + key + ")", dossiers.size(), (int) entry
					.getValue());
		}
	}

	public void testDossiersAideCreee() throws Exception {
		PublicStatutMap<Integer> result = INSTANCE.getDossiersAideCreee(sql,
				params);
		basicAssertions(result);

		for (Map.Entry<PublicStatutMap.Key, Integer> entry : result.entrySet()) {
			PublicStatutMap.Key key = entry.getKey();

			List<Aide> aides = aides(key, params);
			aides = sql.filtrer(aides, new Check<Aide>() {
				public boolean check(Aide aide) {
					return intersection(aide.getDebut(), aide.getDebut(),
							params.getDebut(), params.getFin());
				}
			});

			Set<AspectAides> dossiers = dossiers(aides);
			assertEquals("Nombre (" + key + ")", dossiers.size(), (int) entry
					.getValue());
		}
	}

	public void testDossiersAideSupprimee() throws Exception {
		PublicStatutMap<Integer> result = INSTANCE.getDossiersAideSupprimee(sql,
				params);
		basicAssertions(result);

		for (Map.Entry<PublicStatutMap.Key, Integer> entry : result.entrySet()) {
			PublicStatutMap.Key key = entry.getKey();

			List<Aide> aides = aides(key, params);
			aides = sql.filtrer(aides, new Check<Aide>() {
				public boolean check(Aide aide) {
					return intersection(aide.getFin(), aide.getFin(), //
							params.getDebut(), params.getFin());
				}
			});

			Set<AspectAides> dossiers = dossiers(aides);
			assertEquals("Nombre (" + key + ")", dossiers.size(), (int) entry
					.getValue());
		}
	}

	public void testPersonnes() throws Exception {
		PublicStatutMap<Integer> result = INSTANCE.getPersonnes(sql, params);
		basicAssertions(result);

		for (Map.Entry<PublicStatutMap.Key, Integer> entry : result.entrySet()) {
			PublicStatutMap.Key key = entry.getKey();

			List<Aide> aides = aides(key, params);

			Set<AspectAides> dossiers = dossiers(aides);
			Set<Personne> personnes = new HashSet<Personne>();
			for (AspectAides dossier : dossiers)
				personnes.addAll(dossier.getDossier().getPersonnes());
			assertEquals("Nombre (" + key + ")", personnes.size(), (int) entry
					.getValue());
		}
	}

	public void testMontantUtilise() throws Exception {
		PublicStatutMap<Integer> result = INSTANCE.getMontantUtilise(sql, params);
		basicAssertions(result);

		int debutYM = DateUtils.anneeMois(params.getDebut());
		int finYM = DateUtils.anneeMois(params.getFin());

		for (Map.Entry<PublicStatutMap.Key, Integer> entry : result.entrySet()) {
			PublicStatutMap.Key key = entry.getKey();

			List<Aide> aides = aides(key, params);
			List<AideResumeMontants> montants = sql.query(
					AideResumeMontants.class, new QuickAnd() //
							.append(createBetween("db:annee_mois", //
									debutYM, finYM)) //
							.append(createIn("aide", aides)) //
							.expr());

			int montantTotal = 0;
			for (AideResumeMontants montant : montants) {
				montantTotal += montant.getMontantUtilise();
			}

			assertEquals("Montant (" + key + ")", montantTotal, //
					(int) entry.getValue());
		}
	}

	private void basicAssertions(PublicStatutMap<Integer> result) {
		assertFalse("Aucun résultat", result.isEmpty());

		for (Map.Entry<PublicStatutMap.Key, Integer> entry : result.entrySet()) {
			PublicStatutMap.Key key = entry.getKey();
			System.out.println(key + " -> " + entry.getValue());
			System.out.flush();
			assertNotNull(key.getStatut());
			assertNotNull(key.getPublic());
			assertNotNull(entry.getValue());
		}
	}

	private List<Aide> aides(PublicStatutMap.Key key,
			BudgetParImpPublicFreqParams params) {
		assertEquals(1, params.getImputations().size());
		Imputation imputation = params.getImputations().iterator().next();

		// Récupération des aides correspondantes
		Expression periode = Expression.fromString(
				"debut <= $fin and fin >= $debut").expWithParameters(
				sql.params() //
						.timestamp("debut", params.getDebut()) //
						.timestamp("fin", params.getFin()));

		Expression expr = new QuickAnd() //
				.append(periode) //
				.equals("statut", key.getStatut()) //
				.equals("nature.imputation", imputation) //
				.equals("public", key.getPublic()) //
				.expr();

		return sql.query(Aide.class, expr);
	}

	private Set<AspectAides> dossiers(List<Aide> aides) {
		Set<AspectAides> dossiers = new HashSet<AspectAides>();
		for (Aide aide : aides)
			dossiers.add(aide.getDossier());
		return dossiers;
	}

}
