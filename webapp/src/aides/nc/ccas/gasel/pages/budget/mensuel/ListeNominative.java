package nc.ccas.gasel.pages.budget.mensuel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.Check;
import nc.ccas.gasel.SqlParams;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.cayenne.DataRow;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;

public abstract class ListeNominative extends BasePage implements PeriodeProps {

	private static String BASE_QUERY = "" //
			+ "SELECT p.nom, p.prenom, na.libelle as na_libelle, fa.libelle as fa_libelle, \n"
			+ "       count_dossier_personnes_ressource(a.dossier_dossier_id) as nb_beneficiaire,\n"
			+ "       SUM(a.montant) as montant_aide,\n"
			+ "       SUM(nb_mois(a.debut, a.fin)) as nb_mois,\n"
			+ "	      SUM(am.montant_bons_inutilise) as inutilise\n"
			+ "  FROM aide a\n"
			+ "  JOIN dossier d ON (d.id = a.dossier_dossier_id)\n"
			+ "  JOIN aide_montants am ON (am.id = a.id)\n"
			+ "  JOIN nature_aide na ON (a.nature_id = na.id)\n"
			+ "  JOIN statut_aide fa ON (a.statut_id = fa.id)\n"
			+ "  JOIN personne p ON (d.chef_famille_id = p.id)\n"
			+ " WHERE na.imputation_id = $imp\n"
			+ "   AND annee_mois BETWEEN $debut AND $fin";

	private static String GROUP_BY = "" // 
			+ " GROUP BY p.nom, p.prenom, na.libelle, fa.libelle,\n"
			+ "          count_dossier_personnes_ressource(a.dossier_dossier_id)";

	private List<DataRow> _tableau;

	@Persist("workflow")
	public abstract NatureAide getArticle();

	@Persist("workflow")
	public abstract StatutAide getStatut();

	@Persist("workflow")
	public abstract TypePublic getTypePublic();

	@Persist("workflow")
	public abstract Imputation getImputation();

	public List<DataRow> getTableau() {
		if (_tableau == null) {
			_tableau = new ArrayList<DataRow>();

			StringBuffer final_query = new StringBuffer(BASE_QUERY);
			SqlParams params = sql.params() //
					.set("imp", getImputation()) //
					.yearMonth("debut", getPeriodeDebut()) //
					.yearMonth("fin", getPeriodeFin());

			if (getArticle() != null) {
				final_query.append(" AND na.id = $nature");
				params.set("nature", getArticle());
			}
			if (getStatut() != null) {
				final_query.append(" AND fa.id = $statut");
				params.set("statut", getStatut());
			}
			if (getTypePublic() != null) {
				final_query.append(" AND a.public_id = $public");
				params.set("public", getTypePublic());
			}

			final_query.append(GROUP_BY);
			final_query.append(" ORDER BY p.nom, p.prenom");

			SQLTemplate query = new SQLTemplate(Aide.class, final_query
					.toString());
			_tableau.addAll(sql.query().rows(query, params));
		}
		return _tableau;
	}

	public List<NatureAide> getNaturesAide() {
		return sql.filtrer(sql.query().enumeration(NatureAide.class),
				new Check<NatureAide>() {
					public boolean check(NatureAide value) {
						return value.getImputation().equals(getImputation());
					}
				});
	}

	public Date getDefaultPeriodeDebut() {
		return DateUtils.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return DateUtils.finAnnee();
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_tableau = null;
	}

	@Override
	public String getTitre() {
		if (getImputation() == null)
			return "Liste nominative";
		return "Liste nominative en "
				+ getImputation().getLibelle().toLowerCase();
	}

}
