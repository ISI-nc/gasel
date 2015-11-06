package nc.ccas.gasel.pages.budget.annuel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.budget.SecteurAide;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;
import nc.ccas.gasel.reports.ReportUtils;

import org.apache.cayenne.DataRow;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.tapestry.IRequestCycle;

public abstract class NbDossierAides extends BasePage implements PeriodeProps {

	private static SQLTemplate QUERY = new SQLTemplate(
			Aide.class,
			"SELECT tp.id AS tp_id, sa.id AS sa_id,\n"
					+ "       dossiers_secteur_public(sa.id, tp.id, $debut, $fin) AS nb\n"
					+ "  FROM type_public tp, secteur_aide sa");

	private List<NbDossiersAidesLigne> _tableau;

	public Date getDefaultPeriodeDebut() {
		return DateUtils.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return DateUtils.finAnnee();
	}

	public List<NbDossiersAidesLigne> getTableau() {
		if (_tableau == null) {
			List<DataRow> rows = sql.query().rows(QUERY, sql.params() //
					.set("debut", getPeriodeDebut()) //
					.set("fin", getPeriodeFin()));

			_tableau = new ArrayList<NbDossiersAidesLigne>();
			for (SecteurAide sa : sort(getSecteursAide()).by("libelle")
					.results()) {
				int saId = sql.idOf(sa);
				NbDossiersAidesLigne ligne = new NbDossiersAidesLigne(sa
						.getLibelle());
				for (DataRow row : rows) {
					if (((Number) row.get("sa_id")).intValue() != saId)
						continue;
					// Récup. du type de public
					TypePublic tp = sql.query().byId(TypePublic.class,
							(Integer) row.get("tp_id"));
					// Définition de la valeur
					ligne.setValue(tp, (Integer) row.get("nb"));
				}
				_tableau.add(ligne);
			}
			// Ligne total (toujours sentiment d'erreur)
			NbDossiersAidesLigne cumul = new NbDossiersAidesLigne("Total");
			ReportUtils.cumule(cumul, _tableau);
			_tableau.add(cumul);
		}
		return _tableau;
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_tableau = null;
	}

	public Set<SecteurAide> getSecteursAide() {
		return sql.query().all(SecteurAide.class);
	}

	public Set<TypePublic> getTypesPublic() {
		return sql.query().all(TypePublic.class);
	}

}
