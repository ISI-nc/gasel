package nc.ccas.gasel.pages.budget.annuel;

import static nc.ccas.gasel.modelUtils.CommonQueries.findById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.agents.budget.BudgetParImpPublicFreq;
import nc.ccas.gasel.agents.budget.params.BudgetParImpPublicFreqParams;
import nc.ccas.gasel.agents.budget.result.PublicStatutMap;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;
import nc.ccas.gasel.reports.ReportUtils;
import nc.ccas.gasel.reports.aides.InterventionAlim;

import org.apache.tapestry.IRequestCycle;

public abstract class AlimOccImm extends BasePage implements PeriodeProps {

	private List<AlimOccImmLigne> _tableau;

	public Date getDefaultPeriodeDebut() {
		return DateUtils.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return DateUtils.finAnnee();
	}

	public List<AlimOccImmLigne> getTableau() {
		if (_tableau == null) {
			BudgetParImpPublicFreq agent = BudgetParImpPublicFreq.INSTANCE;

			// params
			BudgetParImpPublicFreqParams params = new BudgetParImpPublicFreqParams(
					this);

			Imputation impAlim = findById(Imputation.class,
					Imputation.ALIMENTATION);
			params.setImputations(impAlim);

			Set<TypePublic> publics = impAlim.getTypesPublic();
			params.setPublics(publics);

			params.setStatuts(StatutAide.OCCASIONNELLE,
					StatutAide.IMMEDIATE);

			// ------

			PublicStatutMap<Integer> dossiers = agent.getDossiers(sql, params);
			PublicStatutMap<Integer> personnes = agent.getPersonnes(sql, params);
			PublicStatutMap<Integer> montantUtilise = agent.getMontantUtilise(
					sql, params);

			// ------

			_tableau = new ArrayList<AlimOccImmLigne>();
			for (TypePublic tp : publics) {
				AlimOccImmLigne ligne = createLigne(tp.getLibelle());
				fill(ligne.getOccasionnel(), tp, StatutAide.OCCASIONNELLE,
						dossiers, personnes, montantUtilise);
				fill(ligne.getImmediat(), tp, StatutAide.IMMEDIATE, dossiers,
						personnes, montantUtilise);
				_tableau.add(ligne);
			}
			Collections.sort(_tableau);

			// XXX Ce cumul est probablement une erreur, mais c'était comme ça
			// (un dossier pouvant être dans plusieurs types de public)
			AlimOccImmLigne total = createLigne("Total");
			ReportUtils.cumule(total, _tableau);
			_tableau.add(total);

		}
		return _tableau;
	}

	private void fill(InterventionAlim interv, TypePublic tp, int freq,
			PublicStatutMap<Integer> dossiers, PublicStatutMap<Integer> personnes,
			PublicStatutMap<Integer> montantUtilise) {
		interv.setNbDossiers(dossiers.get(tp, freq));
		interv.setNbPersonnes(personnes.get(tp, freq));
		interv.setMontant(montantUtilise.get(tp, freq));
	}

	protected AlimOccImmLigne createLigne(String libelle) {
		return new AlimOccImmLigne(libelle);
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_tableau = null;
	}

}
