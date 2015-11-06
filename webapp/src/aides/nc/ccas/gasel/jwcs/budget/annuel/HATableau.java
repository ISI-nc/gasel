package nc.ccas.gasel.jwcs.budget.annuel;

import static java.lang.String.valueOf;
import static nc.ccas.gasel.model.budget.Imputation.ALIMENTATION;
import static nc.ccas.gasel.modelUtils.CommonQueries.findById;
import static nc.ccas.gasel.modelUtils.CommonQueries.getAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.budget.SecteurAide;
import nc.ccas.gasel.pages.budget.annuel.HAPage;
import nc.ccas.gasel.utils.QuickHashMap;

import org.apache.cayenne.DataRow;
import org.apache.cayenne.ObjectContext;
import org.apache.tapestry.IRequestCycle;

public abstract class HATableau extends BaseComponent {

	private List<HALigne> _tableau;

	public abstract Integer getAnnee();

	public List<HALigne> getTableau() {
		if (_tableau == null) {
			ObjectContext dc = getObjectContext();

			// Récupération des secteurs et natures
			List<SecteurAide> secteurs = getPage().sort(
					getAll(dc, SecteurAide.class)).by("libelle").results();
			List<NatureAide> natures = getPage().sort(
					getAll(dc, NatureAide.class)).by("libelle").results();

			// Requète
			int annee = getAnnee();

			String agregation = ((HAPage) getPage()).agregation();
			String query = "SELECT sa.id AS sa_id, na.id AS na_id, mois, "
					+ agregation
					+ "::integer AS valeur\n"
					+ "  FROM aide a\n"
					+ "  JOIN aide_montants am ON (a.id = am.id)\n"
					+ "  JOIN nature_aide na ON (a.nature_id = na.id)\n"
					+ "  JOIN secteur_aide sa ON (na.parent_id = sa.id)\n"
					+ " WHERE am.annee = $annee\n"
					+ "   AND na.imputation_id <> " + ALIMENTATION + "\n"
					+ "   AND (montant_bons_annule IS NULL OR a.montant > montant_bons_annule)\n"
					+ " GROUP BY sa.id, na.id, mois";

			List<DataRow> results = getPage().sql.query().rows(
					query,
					new QuickHashMap<String, String>().put("annee",
							valueOf(annee)).map());

			// Analyse
			HALigne total = new HALigne("Total", true);
			Map<SecteurAide, HALigne> parSecteur = new HashMap<SecteurAide, HALigne>();
			Map<NatureAide, HALigne> parNature = new HashMap<NatureAide, HALigne>();

			for (DataRow row : results) {
				SecteurAide secteur = findById(SecteurAide.class, (Integer) row
						.get("sa_id"), dc);
				NatureAide nature = findById(NatureAide.class, (Integer) row
						.get("na_id"), dc);

				HALigne ligneSecteur = parSecteur.get(secteur);
				HALigne ligneNature = parNature.get(nature);
				if (ligneSecteur == null) {
					ligneSecteur = new HALigne(secteur.getLibelle(), true);
					ligneSecteur.setParent(total);
					parSecteur.put(secteur, ligneSecteur);
				}
				if (ligneNature == null) {
					ligneNature = new HALigne(nature.getLibelle());
					ligneNature.setParent(ligneSecteur);
					parNature.put(nature, ligneNature);
				}

				Integer mois = numAsInt(row.get("mois"));
				Integer valeur = numAsInt(row.get("valeur"));
				if (valeur == null)
					valeur = 0;

				ligneNature.add(mois, valeur);
				ligneSecteur.add(mois, valeur);
				total.add(mois, valeur);
			}

			_tableau = new ArrayList<HALigne>(parSecteur.size()
					+ parNature.size());
			for (SecteurAide secteur : secteurs) {
				HALigne ligneSecteur = parSecteur.get(secteur);
				if (ligneSecteur == null)
					continue;

				for (NatureAide nature : natures) {
					if (!nature.getParent().equals(secteur))
						continue;
					HALigne ligneNature = parNature.get(nature);
					if (ligneNature == null)
						continue;

					_tableau.add(ligneNature);
				}

				_tableau.add(ligneSecteur);
			}

			_tableau.add(total);
		}
		return _tableau;
	}

	private Integer numAsInt(Object object) {
		if (object == null) {
			return null;
		}
		return ((Number) object).intValue();
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_tableau = null;
	}

}
