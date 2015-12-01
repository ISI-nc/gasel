package nc.ccas.gasel.pages.budget.annuel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.budget.SecteurAide;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.modelUtils.SqlUtils;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataRow;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.tapestry.IRequestCycle;

public abstract class HATypePublic extends BasePage implements PeriodeProps {

	private List<HATypePublicLigne> _tableau;

	private List<TypePublic> _typesPublic;

	private List<NatureAide> _naturesAide;

	public Date getDefaultPeriodeDebut() {
		return DateUtils.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return DateUtils.finAnnee();
	}

	private static final SQLTemplate QUERY = new SQLTemplate(
			Aide.class,

			"SELECT n.id AS nature_id,\n"
					+ "       a.public_id AS public_id,\n"
					+ "       annee,\n"
					+ "       mois,\n"
					+ "       COUNT(*) AS nombre,\n"
					+ "       SUM(montant_utilise) AS montant\n"
					+ "\n"
					+ "  FROM aide a\n"
					+ "  JOIN nature_aide n ON (a.nature_id = n.id AND n.imputation_id <> "
					+ Imputation.ALIMENTATION
					+ ")\n"
					+ "  JOIN aide_montants am ON (a.id = am.id\n"
					+ "                                 AND annee_mois BETWEEN $debut AND $fin)\n"
					+ "\n"
					+ " WHERE montant_bons_annule IS NULL OR a.montant > montant_bons_annule\n"
					+ " GROUP BY n.id, a.public_id, annee, mois"

	);

	public List<HATypePublicLigne> getTableau() {
		if (_tableau == null) {
			// Préparation du total
			HATypePublicLigne total = new HATypePublicLigne("Total", true);
			// Préparation des lignes secteurs
			Map<SecteurAide, HATypePublicLigne> parSecteur = new HashMap<SecteurAide, HATypePublicLigne>();
			// Préparation des lignes natures
			Map<NatureAide, HATypePublicLigne> parNature = new HashMap<NatureAide, HATypePublicLigne>();

			Map<Integer, NatureAide> naturesParId = buildIdMap(NatureAide.class);
			Map<Integer, TypePublic> publicParId = buildIdMap(TypePublic.class);

			// Remplissage du tableau
			List<DataRow> rows = doQuery();
			for (DataRow row : rows) {
				NatureAide nature = naturesParId.get(row.get("nature_id"));
				if (nature == null)
					throw new Error("Pas de nature #" + row.get("nature_id"));
				if (!parNature.containsKey(nature)) {
					HATypePublicLigne ligne = new HATypePublicLigne(nature
							.getLibelle(), false);
					ligne.setReference(parSecteur.get(nature.getParent()));
					parNature.put(nature, ligne);
					if (!parSecteur.containsKey(nature.getParent())) {
						SecteurAide secteur = nature.getParent();
						ligne = new HATypePublicLigne("Tot. "
								+ secteur.getLibelle(), true);
						ligne.setReference(total);
						parSecteur.put(secteur, ligne);
					}
				}
				TypePublic tp = publicParId.get(row.get("public_id"));
				int nombre = SqlUtils.toInt(row.get("nombre"));
				int valeur = SqlUtils.toInt(row.get("montant"));
				parNature.get(nature).merge(tp, nombre, valeur);
			}
			_naturesAide = sort(parNature.keySet()).by("libelle").results();

			// Création du tableau
			_tableau = new ArrayList<HATypePublicLigne>();
			for (SecteurAide secteur : sort(parSecteur.keySet()).by("libelle")
					.results()) {
				HATypePublicLigne totalSecteur = parSecteur.get(secteur);
				boolean hasNature = false;
				for (NatureAide nature : sort(secteur.getNatures()).by(
						"libelle").results()) {
					if (nature.getImputation() == null)
						continue;
					if (nature.getImputation().isAlimentation())
						continue;
					if (!parNature.containsKey(nature)) {
						_tableau.add(new HATypePublicLigne(nature.getLibelle(),
								false));
						continue;
					}
					HATypePublicLigne row = parNature.get(nature);
					_tableau.add(row);
					hasNature = true;

					totalSecteur.cumule(row);
					total.cumule(row);
				}
				if (!hasNature)
					continue;

				_tableau.add(totalSecteur);
			}
			_tableau.add(total);
		}
		return _tableau;
	}

	private <T extends DataObject> Map<Integer, T> buildIdMap(Class<T> clazz) {
		return buildIdMap(sql.query().all(clazz));
	}

	private <T extends Persistent> Map<Integer, T> buildIdMap(
			Iterable<T> valeurs) {
		Map<Integer, T> idMap = new HashMap<Integer, T>();
		for (T valeur : valeurs) {
			idMap.put(sql.idOf(valeur), valeur);
		}
		return idMap;
	}

	public List<TypePublic> getTypesPublic() {
		if (_typesPublic == null) {
			_typesPublic = sort(sql.query().enumeration(TypePublic.class)).by(
					"libelle").results();
		}
		return _typesPublic;
	}

	public List<NatureAide> getNaturesAide() {
		if (_naturesAide == null) {
			getTableau();
			// _naturesAide = sort(sql.query().enumeration(NatureAide.class)) //
			// .by("libelle").results();
		}
		return _naturesAide;
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_tableau = null;
		_typesPublic = null;
		_naturesAide = null;
	}

	private List<DataRow> doQuery() {
		return sql.query().rows(QUERY, sql.params() //
				.yearMonth("debut", getPeriodeDebut()) //
				.yearMonth("fin", getPeriodeFin()) //
				);
	}

}
