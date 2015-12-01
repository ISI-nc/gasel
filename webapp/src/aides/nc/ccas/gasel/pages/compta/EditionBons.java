package nc.ccas.gasel.pages.compta;

import static com.asystan.common.cayenne_new.QueryFactory.createAnd;
import static com.asystan.common.cayenne_new.QueryFactory.createBetween;
import static com.asystan.common.cayenne_new.QueryFactory.createEquals;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.reports.ReportUtils;
import nc.ccas.gasel.services.reports.ReportService;

import org.apache.cayenne.DataRow;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;

public abstract class EditionBons extends BasePage {

	private static final String FREQS = "(" + StatutAide.PLURIMENSUELLE
			+ "," + StatutAide.OCCASIONNELLE + ")";

	private static final String QUERY = "SELECT	na.imputation_id AS imputation_id,\n" //
			+ "	na.id AS nature_id,\n" //
			+ "	a.public_id AS public_id,\n" //
			+ "	COUNT(*) AS count_aides,\n" //
			+ "	SUM(count_aide_bons_a_editer(a.id,$debut,$fin)\n" //
			+ "	) AS count_bons,\n" //
			+ "	SUM(a.montant\n" //
			+ "	   * count_aide_bons_a_editer(a.id,$debut,$fin)\n" //
			+ "	   / a.quantite_mensuelle\n" //
			+ "	) AS montant\n" //
			+ "\n" //
			+ "  FROM aide a\n" //
			+ "  JOIN nature_aide na ON (na.id = a.nature_id)\n" //
			+ "  JOIN imputation i ON (i.id = na.imputation_id)\n" //
			+ "  JOIN type_public tp ON (tp.id = a.public_id)\n" //
			+ " WHERE a.statut_id IN "
			+ FREQS //
			+ "   AND (debut <= $fin OR debut IS NULL)"
			+ "   AND (fin >= $debut OR fin   IS NULL)"
			+ "   {{filtre-imputation}}"
			+ "   AND count_aide_bons_a_editer(a.id,$debut,$fin) > 0\n" //
			+ "GROUP BY na.imputation_id, na.id, a.public_id,\n" //
			+ "         i.libelle, na.libelle, tp.libelle\n" //
			+ "ORDER BY i.libelle, na.libelle, tp.libelle";

	@Persist("workflow")
	public abstract Date getMois();

	@Persist("workflow")
	public abstract Imputation getImputation();

	private List<Map<String, Object>> _tableau;

	public List<Map<String, Object>> getTableau() {
		if (_tableau == null) {
			List<DataRow> rows = sql.query().rows(filtreImputation(QUERY),
					sql.params() //
							.set("debut", dates.debutMois(getMois())) //
							.set("fin", dates.finMois(getMois())) //
					);
			_tableau = sql.translator(rows) //
					.translate("imputation_id", Imputation.class) //
					.translate("nature_id", NatureAide.class) //
					.translate("public_id", TypePublic.class) //
			.result;
		}
		return _tableau;
	}

	public Map<String, Object> getTotal() {
		return ReportUtils.cumule(getTableau());
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_tableau = null;
	}

	/*
	 * Edition
	 */
	private static final String AIDES_AVEC_BON_A_CREER = "SELECT a.*"
			+ "  FROM aide a" //
			+ "  JOIN nature_aide na ON (na.id = a.nature_id)\n" //
			+ " WHERE statut_id IN " + FREQS
			+ "   AND (debut <= $fin OR debut IS NULL)"
			+ "   AND (fin >= $debut OR fin   IS NULL)"
			+ "   {{filtre-imputation}}"
			+ "   AND count_aide_bons_a_editer(a.id,$debut,$fin) > 0";

	public void editer(IRequestCycle cycle) {
		DataContext context = getObjectContext();
		DataContext.bindThreadDataContext(context);

		Date debut = dates.debutMois(getMois());
		Date fin = dates.finMois(getMois());

		// Création des bons manquants
		List<Aide> aides = sql.query().objects(
				filtreImputation(AIDES_AVEC_BON_A_CREER),
				sql.params() //
						.set("debut", debut) //
						.set("fin", fin) //
				);

		CommonQueries.prefetch(context, aides, "dossier", "dossier.dossier",
				"dossier.dossier.chefFamille");

		aides = sort(aides) //
				.by("dossier.dossier.chefFamille.nom") //
				.by("dossier.dossier.chefFamille.prenom") //
				.by(new Comparator<Aide>() {
					public int compare(Aide o1, Aide o2) {
						return sql.idOf(o1.getDossier()).compareTo(
								sql.idOf(o2.getDossier()));
					}
				}).results();

		Utilisateur user = getLogin().getUser();
		for (Aide aide : aides) {
			aide.creerBons(debut, user, aide.getDossier().getDossier()
					.getChefFamille());
		}
		try {
			context.commitChanges();
		} catch (Exception ex) {
			context.rollbackChanges();
			throw new RuntimeException(ex);
		}

		// Liste des bons à éditer
		List<Bon> bons = bonsAEditer();
		marquerEdite(bons);

		if (bons.isEmpty()) {
			throw new RuntimeException("Aucun bon à éditer.");
		}
		if (bons.size() > 5000) {
			throw new RuntimeException("Trop de bons à éditer (" + bons.size()
					+ ").");
		}
		ReportService.invoke(cycle, "edition-bons", "BONS_ID",
				CayenneUtils.collectIds(bons));
	}

	private SQLTemplate filtreImputation(String query) {
		String condition;
		if (getImputation() == null) {
			condition = "";
		} else {
			condition = "AND na.imputation_id = " + getImputation().getId();
		}
		return new SQLTemplate(Aide.class, //
				query.replace("{{filtre-imputation}}", condition));
	}

	private List<Bon> bonsAEditer() {
		return sql.query(
				Bon.class,
				createAnd( //
						createBetween("debut", dates.debutMois(getMois()),
								dates.finMois(getMois())), //
						createEquals("etat", EtatBon.CREE)));
	}

	private void marquerEdite(List<Bon> bons) {
		for (Bon bon : bons) {
			bon.setEtat(EtatBon.EDITE);
			triggerModified(bon);
		}
		try {
			getObjectContext().commitChanges();
		} catch (Exception ex) {
			getObjectContext().rollbackChanges();
			throw new RuntimeException(ex);
		}
	}

}
