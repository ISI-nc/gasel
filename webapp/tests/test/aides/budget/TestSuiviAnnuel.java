package test.aides.budget;

import static nc.ccas.gasel.modelUtils.CommonQueries.unique;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.ResumeBudgetaire;
import nc.ccas.gasel.agents.budget.SuiviAnnuel;
import nc.ccas.gasel.model.budget.BudgetImpAnnuel;
import nc.ccas.gasel.model.budget.BudgetImpMensuel;
import nc.ccas.gasel.model.vues.AideResumeMontants;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.sql.QuickAnd;

import org.apache.cayenne.access.DataContext;


public class TestSuiviAnnuel extends TestCase {

	private static int ANNEE = 2009;

	private static int MOIS = Calendar.FEBRUARY;

	private static SuiviAnnuel SUIVI = SuiviAnnuel.INSTANCE;

	static {
	}

	private Date[] periode = DateUtils.mois(ANNEE, MOIS);

	private Date debut = periode[0];

	private Date fin = periode[1];

	private DataContext dc;

	private BasePageSql sql;

	public void testSuiviMontantEngage() throws Exception {
		doTest(new Check() {

			public String subject() {
				return "Montant engagé";
			}

			public double expected(List<AideResumeMontants> montants)
					throws Exception {
				double totalEngage = 0;
				for (AideResumeMontants montant : montants) {
					totalEngage += montant.getMontantEngage();
				}
				return totalEngage;
			}

			public double actualValue(ResumeBudgetaire ligne) throws Exception {
				return ligne.getEngage();
			}

		});
	}

	public void testSuiviMontantUtilise() throws Exception {
		doTest(new Check() {

			public String subject() {
				return "Total bons";
			}

			public double expected(List<AideResumeMontants> montants)
					throws Exception {
				double totalBons = 0;
				for (AideResumeMontants montant : montants) {
					totalBons += montant.getMontantBons();
				}
				return totalBons;
			}

			public double actualValue(ResumeBudgetaire ligne) throws Exception {
				return ligne.getTotalBons();
			}

		});
	}

	private void doTest(Check check) throws Exception {
		Map<BudgetImpAnnuel, ResumeBudgetaire> suiviMap = resultats();

		for (Map.Entry<BudgetImpAnnuel, ResumeBudgetaire> entry : suiviMap
				.entrySet()) {
			BudgetImpAnnuel bia = entry.getKey();
			assertFalse(bia.getMensuels().isEmpty());

			ResumeBudgetaire ligne = entry.getValue();

			BudgetImpMensuel bim = bia.budgetMensuel(MOIS);

			double expected = bim == null ? 0 : check.expected(bim
					.getAideMontants());
			double actual = check.actualValue(ligne);

			System.out.printf("%4d / %-20s / %3s --> exp: %7d / act: %7d\n",
					bia.getParent().getAnnee(), bia.getImputation(), //
					bim == null ? "N/A" : bim.getMois() + 1, //
					(int) expected, (int) actual);
			System.out.flush();

			assertEquals(check.subject() + " (" + bia.getImputation()
					+ ", delta=" + (expected - actual) + ")", //
					expected, actual);
		}
	}

	static interface Check {
		String subject();

		double expected(List<AideResumeMontants> montants) throws Exception;

		double actualValue(ResumeBudgetaire ligne) throws Exception;
	}

	private Map<BudgetImpAnnuel, ResumeBudgetaire> resultats() {
		List<ResumeBudgetaire> suivi = SUIVI.getSuivi(sql, debut, fin);
		Map<BudgetImpAnnuel, ResumeBudgetaire> suiviMap = new HashMap<BudgetImpAnnuel, ResumeBudgetaire>();

		for (ResumeBudgetaire ligne : suivi) {
			String libelle = ligne.getLibelle();
			if ("Total".equals(libelle))
				continue;

			// Recherche du budget
			BudgetImpAnnuel bia = findBIA(libelle);

			assertNotNull("BIA non trouvé : " + libelle, bia);

			suiviMap.put(bia, ligne);
		}
		return suiviMap;
	}

	private BudgetImpAnnuel findBIA(String libelle) {
		return unique(dc, BudgetImpAnnuel.class, new QuickAnd() //
				.equals("parent.annee", ANNEE) //
				.equals("imputation.libelle", libelle) //
				.expr());
	}

	@Override
	protected void setUp() throws Exception {
		dc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(dc);
		sql = new BasePageSql(dc);
	}

}
