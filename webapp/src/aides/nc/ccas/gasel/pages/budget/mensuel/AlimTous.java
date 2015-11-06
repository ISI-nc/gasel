package nc.ccas.gasel.pages.budget.mensuel;

import static nc.ccas.gasel.modelUtils.CommonQueries.findById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.agents.budget.DossiersPersonnesMontantParPublic;
import nc.ccas.gasel.agents.budget.result.DossiersPersonnesMontant;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;
import nc.ccas.gasel.reports.ReportUtils;
import nc.ccas.gasel.reports.aides.InterventionAlim;

import org.apache.cayenne.query.SQLTemplate;
import org.apache.tapestry.IRequestCycle;

public abstract class AlimTous extends BasePage implements PeriodeProps {

	private static final SQLTemplate QUERY = new SQLTemplate(
			Aide.class,
			"SELECT imp.id AS imp_id, tp.id AS tp_id,\n"
					+ "       dossiers_imp_public(imp.id,tp.id,$debut,$fin) AS nb_dossiers,\n"
					+ "       dossiers_personnes_imp_public(imp.id,tp.id,$debut,$fin) AS nb_personnes,\n"
					+ "       aides_montant_engage_imp_public(imp.id,tp.id,$debut,$fin,$split) AS montant\n"
					+ "  FROM type_public tp,\n" //
					+ "       imputation imp\n"
					+ " WHERE tp.id IN $types_public\n"
					+ "   AND imp.id IN $imp_id\n" //
					+ "GROUP BY imp.id, tp.id");
	static {
		QUERY.setFetchingDataRows(true);
	}

	private List<InterventionAlim> _tableau;

	public Date getDefaultPeriodeDebut() {
		return DateUtils.debutMois();
	}

	public Date getDefaultPeriodeFin() {
		return DateUtils.finMois();
	}

	public List<InterventionAlim> getTableau() {
		if (_tableau == null) {
			DossiersPersonnesMontantParPublic dpmpp = DossiersPersonnesMontantParPublic.INSTANCE;

			Imputation impAlim = findById(Imputation.class,
					Imputation.ALIMENTATION);
			Set<TypePublic> publics = impAlim.getTypesPublic();

			Map<TypePublic, DossiersPersonnesMontant> values = dpmpp
					.getDossierPersonnesMontantParPublic(sql, this, publics,
							impAlim);

			_tableau = new ArrayList<InterventionAlim>();
			for (TypePublic tp : publics) {
				InterventionAlim ligne = new InterventionAlim(tp.getLibelle());
				DossiersPersonnesMontant value = values.get(tp);
				ligne.setTitre(tp.getLibelle());
				ligne.setNbDossiers(value.getNbDossiers());
				ligne.setNbPersonnes(value.getNbPersonnes());
				ligne.setMontant(value.getMontant());
				_tableau.add(ligne);
			}
			Collections.sort(_tableau);

			// XXX Ce cumul est probablement une erreur, mais c'était comme ça
			// (un dossier pouvant être dans plusieurs types de public)
			InterventionAlim total = new InterventionAlim("Total");
			ReportUtils.cumule(total, _tableau);
			total.setHighlight(true);
			_tableau.add(total);
		}
		return _tableau;
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_tableau = null;
	}

}
