package nc.ccas.gasel.pages.pi;

import static nc.ccas.gasel.modelUtils.DateUtils.inter;
import static org.apache.cayenne.exp.Expression.fromString;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import java_gaps.Interval;
import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.Check;
import nc.ccas.gasel.model.pi.ArreteJF;
import nc.ccas.gasel.model.pi.AspectDossierPI;
import nc.ccas.gasel.model.pi.AttributionJF;
import nc.ccas.gasel.model.pi.JardinFamilial;
import nc.ccas.gasel.model.pi.Parcelle;
import nc.ccas.gasel.model.pi.enums.OrigineDemandeJF;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.parser.ASTObjPath;
import org.apache.tapestry.IRequestCycle;

public abstract class TableauDeBord extends BasePage implements PeriodeProps {

	private List<JardinFamilial> _jardins;
	private Collection<ArreteJF> _arretesPeriode;

	public abstract JardinFamilial getRow();

	public abstract Date getMois();

	public abstract OrigineDemandeJF getOrigineDemande();

	public Date getDefaultPeriodeDebut() {
		return dates.debutMois();
	}

	public Date getDefaultPeriodeFin() {
		return dates.finMois();
	}

	public List<JardinFamilial> getJardins() {
		if (_jardins == null) {
			Set<JardinFamilial> jardins = sql.query() //
					.all(JardinFamilial.class, "parcelles", "demandes");
			_jardins = sort(jardins).by("nom").results();
		}
		return _jardins;
	}

	private static final DateFormat FORMAT_MOIS = new SimpleDateFormat("MM/yy");

	public DateFormat getFormatMois() {
		return FORMAT_MOIS;
	}

	public List<Date> getMoisPeriode() {
		List<Date> mois = new LinkedList<Date>();
		Date fin = getPeriodeFin();
		GregorianCalendar gc = new GregorianCalendar();
		for (gc.setTime(getPeriodeDebut()); gc.getTime().compareTo(fin) <= 0; gc
				.add(Calendar.MONTH, 1)) {
			mois.add(gc.getTime());
		}
		return mois;
	}

	public List<OrigineDemandeJF> getOriginesDemande() {
		return sql.query().enumeration(OrigineDemandeJF.class);
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_jardins = null;
		_arretesPeriode = null;
	}

	// ------------------------------------------------------------------------
	// Affectations des parcelles
	//

	public int getNbFoyers() {
		return agrege("attribution.demande.dossier").size();
	}

	public int getNbBeneficiaires() {
		int result = 0;
		for (AspectDossierPI aspect : agrege(AspectDossierPI.class,
				"attribution.demande.dossier")) {
			result += aspect.getDossier().getNbPersonnes();
		}
		return result;
	}

	public int getNbCollectivites() {
		return agrege("attribution.demande.collectivite").size();
	}

	public int getNbNonAffectees() {
		List<Parcelle> parcellesJardin = getRow().getParcelles();
		return parcellesJardin.size() - agrege("attribution.parcelle").size();
	}

	private Set<Object> agrege(String path) {
		return agrege(Object.class, path);
	}

	private <T> Set<T> agrege(Class<T> clazz, String path) {
		return agrege(new ObjPathTranslator<T>(clazz, path));
	}

	private <T> Set<T> agrege(Translator<? super ArreteJF, T> translator) {
		Set<T> values = new HashSet<T>();
		for (ArreteJF arrete : arretesPeriodeRow()) {
			T value = translator.translator(arrete);
			if (value == null)
				continue;
			values.add(value);
		}
		return values;
	}

	// ------------------------------------------------------------------------
	// Arretés
	//

	public int getNbRenouvellement() {
		int result = 0;
		for (ArreteJF a : arretesPeriodeRow()) {
			if (a.getType() != null && a.getType().getRenouvellement())
				result++;
		}
		return result;
	}

	public int getNbNouveaux() {
		int result = 0;
		for (ArreteJF a : arretesPeriodeRow()) {
			if (a.getType() == null || !a.getType().getRenouvellement())
				result++;
		}
		return result;
	}

	public int getNbArretes() {
		return arretesPeriodeRow().size();
	}

	private Collection<ArreteJF> arretesPeriodeRow() {
		return sql.filtrer(arretesPeriode(), new Check<ArreteJF>() {
			public boolean check(ArreteJF value) {
				JardinFamilial jardin = value.getAttribution().getParcelle()
						.getJardin();
				return jardin.equals(getRow());
			}
		});
	}

	private Collection<ArreteJF> arretesPeriode() {
		if (_arretesPeriode == null) {
			Expression expr = fromString("debut<=$fin and fin>=$debut");
			expr = expr.expWithParameters(sql.params() //
					.timestamp("debut", getPeriodeDebut()) //
					.timestamp("fin", getPeriodeFin()) //
					);
			_arretesPeriode = sql.query(ArreteJF.class, expr, //
					"attribution.parcelle.jardin", //

					"attribution.demande.dossier", //
					"attribution.demande.dossier.dossier.chefFamille", //
					"attribution.demande.dossier.dossier.conjoint", //
					"attribution.demande.dossier.dossier.personnesACharge", //
					"attribution.demande.dossier.dossier.personnesNonACharge", //

					"attribution.demande.collectivite", //

					"attribution.demande.origine", //
					"attribution.paiements" //
			);
		}
		return _arretesPeriode;
	}

	// ------------------------------------------------------------------------
	// Origine des demandes
	//

	/**
	 * @return Le nombre d'arrêtés durant la période en cours sur le jardin en
	 *         cours (la ligne) et dont l'origine de la demande est l'origine en
	 *         cours (la colonne).
	 */
	public int getNbDemande() {
		OrigineDemandeJF origine = getOrigineDemande();
		Set<ArreteJF> values = new HashSet<ArreteJF>();
		for (ArreteJF arrete : arretesPeriodeRow()) {
			if (!arrete.getAttribution().getDemande().getOrigine() //
					.equals(origine))
				continue;

			values.add(arrete);
		}
		return values.size();
	}

	// ------------------------------------------------------------------------
	// Taux de recouvrement
	//

	/**
	 * @return Le taux de recouvrement du jardin (<code>row</code>) en cours
	 *         pour le mois en cours (<code>mois</code>).
	 */
	public Double getTauxRecouvrement() {
		Collection<ArreteJF> arretes = arretesPeriodeRow();

		Date debutMois = dates.debutMois(getMois());
		Date finMois = dates.finMois(debutMois);

		Set<AttributionJF> attributionsActives = new HashSet<AttributionJF>();
		for (ArreteJF arrete : arretes) {
			Interval<Date> intersection = inter(debutMois, finMois, //
					arrete.getDebut(), arrete.getFin());
			if (intersection == null)
				continue;

			attributionsActives.add(arrete.getAttribution());
		}
		if (attributionsActives.isEmpty())
			return null;

		int attributionsPayees = 0;
		for (AttributionJF attribution : attributionsActives) {
			Date payeJusquA = attribution.getPayeJusquA();
			if (payeJusquA == null)
				continue; // pas payé

			if (!payeJusquA.after(finMois))
				continue; // paiement en retard

			attributionsPayees++;
		}

		return ((double) attributionsPayees) / attributionsActives.size();
	}
}

/**
 * 
 * @param <F>
 *            From
 * @param <T>
 *            To
 */
interface Translator<F, T> {

	public T translator(F source);

}

class ObjPathTranslator<T> implements Translator<DataObject, T> {

	private final ASTObjPath path;
	private final Class<T> clazz;

	public ObjPathTranslator(Class<T> clazz, String path) {
		this.clazz = clazz;
		this.path = new ASTObjPath(path);
	}

	public T translator(DataObject source) {
		return clazz.cast(path.evaluate(source));
	}

}