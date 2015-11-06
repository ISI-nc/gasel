package nc.ccas.gasel.pages.budget.annuel;

import static nc.ccas.gasel.model.aides.StatutAide.PLURIMENSUELLE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.agents.budget.BudgetParImpPublicFreq;
import nc.ccas.gasel.agents.budget.params.BudgetParImpPublicFreqParams;
import nc.ccas.gasel.agents.budget.result.PublicStatutMap;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.tapestry.IRequestCycle;

public abstract class AlimPlurimensuel extends BasePage implements PeriodeProps {

	private List<AlimPlurimensuelLigne> _tableau;

	public Date getDefaultPeriodeDebut() {
		return DateUtils.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return DateUtils.finAnnee();
	}

	public List<AlimPlurimensuelLigne> getTableau() {
		if (_tableau == null) {
			_tableau = new ArrayList<AlimPlurimensuelLigne>();

			AlimPlurimensuelLigne actifs = new AlimPlurimensuelLigne(
					"Dossiers actifs");
			AlimPlurimensuelLigne supprimes = new AlimPlurimensuelLigne(
					"Dossiers avec aide supprimée");
			AlimPlurimensuelLigne crees = new AlimPlurimensuelLigne(
					"Dossiers avec aide créée");
			AlimPlurimensuelLigne administres = new AlimPlurimensuelLigne(
					"Total des administrés");
			AlimPlurimensuelLigne montants = new AlimPlurimensuelLigne(
					"Montant des aides");
			
			BudgetParImpPublicFreq agent = BudgetParImpPublicFreq.INSTANCE;
			BudgetParImpPublicFreqParams params;
			params = new BudgetParImpPublicFreqParams(this);
			params.setImputations(Imputation.ALIMENTATION);
			params.setStatuts(PLURIMENSUELLE);
			params.setPublics(getTypesPublic());

			PublicStatutMap<Integer> dossiers = agent.getDossiers(sql, params);
			PublicStatutMap<Integer> dossiersAideCreee = agent.getDossiersAideCreee(sql, params);
			PublicStatutMap<Integer> dossiersAideSupprimee = agent.getDossiersAideSupprimee(sql, params);
			PublicStatutMap<Integer> personnes = agent.getPersonnes(sql, params);
			PublicStatutMap<Integer> montantUtilise = agent.getMontantUtilise(sql, params);

			for (TypePublic tp : getTypesPublic()) {
				actifs.setQuantite(tp, dossiers.get(tp, PLURIMENSUELLE));
				supprimes.setQuantite(tp, dossiersAideCreee.get(tp, PLURIMENSUELLE));
				crees.setQuantite(tp, dossiersAideSupprimee.get(tp, PLURIMENSUELLE));
				administres.setQuantite(tp, personnes.get(tp, PLURIMENSUELLE));
				montants.setQuantite(tp, montantUtilise.get(tp, PLURIMENSUELLE));
			}

			_tableau.add(actifs);
			_tableau.add(supprimes);
			_tableau.add(crees);
			_tableau.add(administres);
			_tableau.add(montants);
		}
		return _tableau;
	}

	private List<TypePublic> _typesPublic;

	public List<TypePublic> getTypesPublic() {
		if (_typesPublic == null) {
			_typesPublic = new ArrayList<TypePublic>();
			_typesPublic.addAll(objectForPk(Imputation.class,
					Imputation.ALIMENTATION).getTypesPublic());
			_typesPublic = sort(_typesPublic).by("libelle").results();
		}

		return _typesPublic;
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_typesPublic = null;
		_tableau = null;
	}

}
