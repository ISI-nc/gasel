package nc.ccas.gasel.pages.budget.annuel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.agents.budget.SuiviParPublicNature;
import nc.ccas.gasel.agents.budget.result.DossiersPersonnesMontant;
import nc.ccas.gasel.agents.budget.result.PublicNatureMap;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.tapestry.IRequestCycle;

public abstract class SuiviAnnuelArticleAlimentation extends BasePage implements
		PeriodeProps {

	private Set<TypePublic> _publics;

	private List<AlimPublicNatureLigne> _tableau;

	public Date getDefaultPeriodeDebut() {
		return DateUtils.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return DateUtils.finAnnee();
	}

	public List<AlimPublicNatureLigne> getTableau() {
		if (_tableau == null) {
			Imputation impAlim = getImputation();
			Set<TypePublic> publics = getTypesPublic();

			PublicNatureMap<DossiersPersonnesMontant> values = SuiviParPublicNature.INSTANCE
					.getSuiviParPublicNature(this, publics, impAlim);

			_tableau = new ArrayList<AlimPublicNatureLigne>();

			for (NatureAide nature : impAlim.getNaturesAide()) {
				AlimPublicNatureLigne ligne = new AlimPublicNatureLigne(nature
						.getLibelle());
				for (TypePublic publik : publics) {
					ligne.setQuantite(publik, values.get(publik, nature));
				}
				_tableau.add(ligne);
			}

			Collections.sort(_tableau);
		}
		return _tableau;
	}

	public Set<TypePublic> getTypesPublic() {
		if (_publics == null) {
			_publics = getImputation().getTypesPublic();
		}
		return _publics;
	}

	private Imputation getImputation() {
		return sql.query().byId(Imputation.class, Imputation.ALIMENTATION);
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_publics = null;
		_tableau = null;
	}

}