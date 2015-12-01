package test.aides.budget;

import static nc.ccas.gasel.modelUtils.CommonQueries.findById;
import static nc.ccas.gasel.modelUtils.CommonQueries.getAll;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.BudgetParImpPublicFreq;
import nc.ccas.gasel.agents.budget.ResumeBudgetaire;
import nc.ccas.gasel.agents.budget.SuiviAnnuel;
import nc.ccas.gasel.agents.budget.params.BudgetParImpPublicFreqParams;
import nc.ccas.gasel.agents.budget.result.PublicStatutMap;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.modelUtils.MoisLibelle;
import nc.ccas.gasel.reports.PeriodePropsBean;

import org.apache.cayenne.access.DataContext;


import com.asystan.common.StringUtils;

public class TestCoherenceSuiviEtImpPublicFreq extends TestCase {

	static {
	}

	private static BudgetParImpPublicFreq BPIPF = BudgetParImpPublicFreq.INSTANCE;

	private static SuiviAnnuel SUIVI = SuiviAnnuel.INSTANCE;

	private BasePageSql sql;

	/*
	 * Tests sur l'année en cours
	 */

	public void testAnneeAlimentation() {
		testAnneeImputation(Imputation.ALIMENTATION);
	}

	public void testAnneeAvance() {
		testAnneeImputation(Imputation.AVANCE);
	}

	public void testAnneeAvanceRemboursable() {
		testAnneeImputation(Imputation.URGENCES);
	}

	public void testAnneeSolidarite() {
		testAnneeImputation(Imputation.SOLIDARITE);
	}

	/*
	 * Tests sur chaque mois de l'année en cours
	 */

	public void testMoisAlimentation() {
		testTousLesMois(Imputation.ALIMENTATION);
	}

	public void testMoisAvance() {
		testTousLesMois(Imputation.AVANCE);
	}

	public void testMoisAvanceRemboursable() {
		testTousLesMois(Imputation.URGENCES);
	}

	public void testMoisSolidarite() {
		testTousLesMois(Imputation.SOLIDARITE);
	}

	private void testTousLesMois(int imputation) throws AssertionFailedError {
		MoisLibelle[] moisLibelles = DateUtils.moisCourts();

		List<String> errors = new LinkedList<String>();
		for (int mois = Calendar.JANUARY; mois <= Calendar.DECEMBER; mois++) {
			// XXX
			if (mois != Calendar.FEBRUARY)
				continue;

			System.out.println("--> " + moisLibelles[mois]);
			try {
				testMoisImputation(imputation, mois);
			} catch (AssertionFailedError afe) {
				errors.add(moisLibelles[mois] + ": " + afe.getMessage());
			}
		}

		if (!errors.isEmpty()) {
			throw new AssertionFailedError("Errors:\n"
					+ StringUtils.join("\n", errors));
		}
	}

	/*
	 * -----
	 */

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		DataContext.bindThreadDataContext(CayenneUtils.createDataContext());
		sql = new BasePageSql(DataContext.getThreadDataContext());
	}

	private void testAnneeImputation(int imputationId) {
		Date debut = DateUtils.debutAnnee();
		Date fin = DateUtils.finAnnee();

		testImputationPeriode(imputationId, debut, fin);
	}

	private void testMoisImputation(int imputationId, int mois) {
		int annee = DateUtils.anneeEnCours();
		Date debut = DateUtils.debutMois(annee, mois);
		Date fin = DateUtils.finMois(annee, mois);

		testImputationPeriode(imputationId, debut, fin);
	}

	private void testImputationPeriode(int imputationId, Date debut, Date fin) {
		Imputation imputation = findById(Imputation.class, imputationId);

		BudgetParImpPublicFreqParams params = new BudgetParImpPublicFreqParams(
				new PeriodePropsBean(debut, fin));
		params.setImputations(imputation);
		params.setStatuts(getAll(StatutAide.class));
		params.setPublics(getAll(TypePublic.class));

		PublicStatutMap<Integer> montantUtilise = BPIPF.getMontantUtilise(sql,
				params);

		int totalBPIPF = 0;
		for (int value : montantUtilise.values()) {
			totalBPIPF += value;
		}

		// -----

		List<ResumeBudgetaire> suivi = SUIVI.getSuivi(sql, debut, fin);

		int totalSuivi = 0;
		for (ResumeBudgetaire resume : suivi) {
			if (!resume.getLibelle().equals(imputation.getLibelle()))
				continue;
			totalSuivi += resume.getTotalBons() - resume.getInutilise();
		}

		assertEquals(
				"Suivi (" + totalSuivi + ") - BPIPF (" + totalBPIPF + "):", 0,
				totalSuivi - totalBPIPF);
	}

}
