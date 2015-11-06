package tests.model.budget;

import static nc.ccas.gasel.modelUtils.CayenneUtils.createDataContext;
import static nc.ccas.gasel.modelUtils.CommonQueries.getAll;
import static nc.ccas.gasel.modelUtils.CommonQueries.select;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;
import nc.ccas.gasel.model.vues.AideResumeMontants;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.cayenne.DataRow;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SQLTemplate;

import tests.model.AllModelTests;

/**
 * Tests sur les vues du budget.
 * 
 * @author isinc
 */
public class TestAideMontants extends TestCase {

	static {
		AllModelTests.setupDatabase();
	}

	/*
	 * Tests sur aide_resume_montants, colonne par colonne.
	 */

	/**
	 * Test aide_resume_montants.id
	 */
	public void testAideResumeMontants_Id() throws Exception {
		for (AideResumeMontants arm : values("testAideResumeMontants_Id")) {
			assertNotNull(arm.getAide());
		}
	}

	/**
	 * Test aide_resume_montants.bim_id
	 */
	public void testAideResumeMontants_BimId() throws Exception {
		for (AideResumeMontants arm : values("testAideResumeMontants_BimId")) {
			assertNotNull(arm.getBim());
		}
	}

	/**
	 * Test aide_resume_montants.public_id
	 */
	public void testAideResumeMontants_PublicId() throws Exception {
		for (AideResumeMontants arm : values("testAideResumeMontants_PublicId")) {
			assertNotNull(arm.getBim());
		}
	}

	/**
	 * Test aide_resume_montants.secteur_id
	 */
	public void testAideResumeMontants_SecteurId() throws Exception {
		for (AideResumeMontants arm : values("testAideResumeMontants_SecteurId")) {
			assertNotNull(arm.getSecteur());
		}
	}

	/**
	 * Test aide_resume_montants.annee
	 */
	public void testAideResumeMontants_Annee() throws Exception {
		for (AideResumeMontants arm : values("testAideResumeMontants_Annee")) {
			assertNotNull(arm.getAnnee());

			Aide aide = arm.getAide();

			assertNotNull(aide.getDebut());
			assertNotNull(aide.getFin());

			Date[] annee = DateUtils.annee(arm.getAnnee());

			assertTrue("Aide[" + date(aide.getDebut()) + ", "
					+ date(aide.getFin()) + "] / annee: " + arm.getAnnee(),
					DateUtils.intersection(aide.getDebut(), aide.getFin(),
							annee[0], annee[1]));
		}
	}

	private static final DateFormat DATE = new SimpleDateFormat("dd/MM/yyyy");

	private String date(Date date) {
		return DATE.format(date);
	}

	/**
	 * Test aide_resume_montants.mois
	 */
	public void testAideResumeMontants_Mois() throws Exception {
		for (AideResumeMontants arm : values("testAideResumeMontants_Mois")) {
			assertNotNull(arm.getMois());

			Date[] mois = DateUtils.mois(arm.getAnnee(), arm.getMois());

			Aide aide = arm.getAide();
			assertTrue(DateUtils.intersection(aide.getDebut(), aide.getFin(),
					mois[0], mois[1]));
		}
	}

	/**
	 * Test aide_resume_montants.montant_aide
	 */
	public void testAideResumeMontants_MontantAide() throws Exception {
		for (AideResumeMontants arm : values("testAideResumeMontants_MontantAide")) {
			assertNotNull(arm.getMontantAide());
			assertEquals(arm.getAide().getMontant(), arm.getMontantAide());
		}
	}

	/**
	 * Test aide_resume_montants.montant_bons
	 */
	public void testAideResumeMontants_MontantBons() throws Exception {
		for (AideResumeMontants arm : values("testAideResumeMontants_MontantBons")) {
			assertNotNull(arm.getMontantBons());
			List<Bon> bons = arm.getBons();

			int sum = 0;
			for (Bon bon : bons) {
				sum += bon.getMontant();
			}

			assertEquals(sum, arm.getMontantBons().intValue());
		}
	}

	/**
	 * Test aide_resume_montants.montant_bons_utilise
	 */
	public void testAideResumeMontants_MontantBonsUtilise() throws Exception {
		for (AideResumeMontants arm : values("testAideResumeMontants_MontantBonsUtilise")) {
			assertNotNull(arm.getMontantBonsUtilise());
			List<Bon> bons = arm.getBons();

			int sum = 0;
			for (Bon bon : bons) {
				if (bon.getUsage() == null)
					continue;
				sum += bon.getUsage().getMontantUtilise();
			}

			assertEquals(arm.getObjectId() + ":\n  - bons: " + bons + "\n-->",
					sum, arm.getMontantBonsUtilise().intValue());
		}
	}

	/**
	 * Test aide_resume_montants.montant_bons_inutilise_brut
	 */
	public void testAideResumeMontants_BonsInutiliseBrut() throws Exception {
		for (AideResumeMontants arm : values("testAideResumeMontants_BonsInutiliseBrut")) {
			assertNotNull(arm.getMontantBonsInutiliseBrut());
			List<Bon> bons = arm.getBons();

			int sum = 0;
			for (Bon bon : bons) {
				if (bon.getUsage() == null)
					continue;

				sum += bon.getMontant() - bon.getUsage().getMontantUtilise();
			}

			assertEquals(sum, arm.getMontantBonsInutiliseBrut().intValue());
		}
	}

	/**
	 * Test aide_resume_montants.montant_bons_inutilise
	 */
	public void testAideResumeMontants_BonsInutilise() throws Exception {
		for (AideResumeMontants arm : values("testAideResumeMontants_BonsInutilise")) {
			assertNotNull(arm.getMontantBonsInutilise());
			List<Bon> bons = arm.getBons();

			int sum = 0;
			for (Bon bon : bons) {
				EtatBon etat = bon.getEtat();
				if (etat.isAnnule()) {
					sum += bon.getMontant();
				} else if (etat.isPartiellementUtilise()) {
					sum += bon.getMontant()
							- bon.getUsage().getMontantUtilise();
				}
			}

			assertEquals(arm.getObjectId() + ": ", sum, arm
					.getMontantBonsInutilise().intValue());
		}
	}

	/**
	 * Test aide_resume_montants.montant_bons_annule
	 */
	public void testAideResumeMontants_MontantBonsAnnule() throws Exception {
		for (AideResumeMontants arm : values("testAideResumeMontants_MontantBonsAnnule")) {
			assertNotNull(arm.getMontantBonsAnnule());
			List<Bon> bons = arm.getBons();

			int sum = 0;
			for (Bon bon : bons) {
				EtatBon etat = bon.getEtat();
				if (!etat.isAnnule())
					continue;
				sum += bon.getMontant();
			}

			assertEquals(sum, arm.getMontantBonsAnnule().intValue());
		}
	}

	/**
	 * Test aide_resume_montants.montant_bons_edite
	 */
	public void testAideResumeMontants_MontantBonsEdite() throws Exception {
		for (AideResumeMontants arm : values("testAideResumeMontants_MontantBonsEdite")) {
			assertNotNull(arm.getMontantBonsEdite());
			List<Bon> bons = arm.getBons();

			int sum = 0;
			for (Bon bon : bons) {
				EtatBon etat = bon.getEtat();
				if (!(etat.isCree() || etat.isEdite()))
					continue;
				sum += bon.getMontant();
			}

			assertEquals(sum, arm.getMontantBonsEdite().intValue());
		}
	}

	public void testAideMontant_MontantUtilise() throws Exception {
		for (AideResumeMontants arm : values("testAideMontant_MontantUtilise")) {
			assertNotNull(arm.getMontantUtilise());
			assertEquals(arm.getObjectId().toString(), arm.getMontantUtilise(), //
					amValue(arm, "montant_utilise"));
		}
	}

	public void testAideMontant_MontantInutilise() throws Exception {
		for (AideResumeMontants arm : values("testAideMontant_MontantInutilise")) {
			assertNotNull(arm.getMontantInutilise());
			assertEquals(arm.getMontantInutilise(), //
					amValue(arm, "montant_inutilise"));
		}
	}

	public void testAideMontant_MontantEngage() throws Exception {
		for (AideResumeMontants arm : values("testAideMontant_MontantEngage")) {
			assertNotNull(arm.getMontantEngage());
			assertEquals(arm.getMontantEngage(), //
					amValue(arm, "montant_engage"));
		}
	}

	/*
	 * ------------------------------------------------------------------------
	 */

	private Number amValue(AideResumeMontants arm, String column) {
		SQLTemplate template = new SQLTemplate(Aide.class, //
				"SELECT " + column
						+ "  FROM gasel_v2.aide_montants" //
						+ " WHERE id = " + arm.getAide().getId()
						+ "   AND annee_mois = " + arm.getAnneeMois());
		template.setFetchingDataRows(true);
		List<?> results = dc.performQuery(template);
		assertEquals("Un seul résultat", 1, results.size());
		DataRow row = (DataRow) results.get(0);
		return (Number) row.get(column);
	}

	private static DataContext dc;

	private static List<AideResumeMontants> values;

	static {
		dc = createDataContext();
		DataContext.bindThreadDataContext(dc);

		if (true) { // test limité (X par an)
			int COUNT = 10;

			Map<Integer, Integer> counts = new TreeMap<Integer, Integer>();
			values = new LinkedList<AideResumeMontants>();
			for (AideResumeMontants arm : getAll(AideResumeMontants.class)) {
				Integer annee = arm.getAnnee();
				if (!counts.containsKey(annee)) {
					counts.put(arm.getAnnee(), 0);
				}
				if (counts.get(annee) >= COUNT)
					continue;
				counts.put(arm.getAnnee(), counts.get(arm.getAnnee()) + 1);
				values.add(arm);
			}
		} else if (false) { // test limité (id=X)
			values = select(AideResumeMontants.class, "db:id = 26941");
		} else if (false) { // test limité (2009)
			values = select(AideResumeMontants.class, "annee = 2009");
		} else {
			values = getAll(AideResumeMontants.class);
		}
	}

	@Override
	protected void setUp() throws Exception {
		DataContext.bindThreadDataContext(dc);
	}

	protected Iterable<AideResumeMontants> values(String prefix) {
		return new ProgressIterable<AideResumeMontants>(values, prefix);
	}

}

class CastingIterator<T> implements Iterator<T> {

	private final Class<T> clazz;

	private final Iterator<?> iterator;

	public CastingIterator(Class<T> clazz, Iterator<?> iterator) {
		this.clazz = clazz;
		this.iterator = iterator;
	}

	public boolean hasNext() {
		return iterator.hasNext();
	}

	public T next() {
		return clazz.cast(iterator.next());
	}

	public void remove() {
		iterator.remove();
	}

}

class ProgressIterable<T> implements Iterable<T> {

	private final Collection<T> collection;

	private final String prefix;

	public ProgressIterable(Collection<T> collection, String prefix) {
		this.collection = collection;
		this.prefix = prefix;
	}

	public Iterator<T> iterator() {
		return new ProgressIterator<T>(collection.iterator(),
				collection.size(), prefix);
	}

}

class ProgressIterator<T> implements Iterator<T> {

	private final Iterator<T> delegate;

	private final int elementCount;

	private int element = 0;

	private byte lastPercent = -1;

	private final String prefix;

	public ProgressIterator(Iterator<T> delegate, int elementCount,
			String prefix) {
		this.delegate = delegate;
		this.elementCount = elementCount;
		this.prefix = prefix;
	}

	public boolean hasNext() {
		return delegate.hasNext();
	}

	public T next() {
		T next = delegate.next();
		element++;
		byte percent = ((byte) (element * 100 / elementCount));
		if (percent > lastPercent) {
			System.out.printf("%s --> %3d%%\r", prefix, percent);
			lastPercent = percent;
		}
		return next;
	}

	public void remove() {
		delegate.remove();
	}

}
