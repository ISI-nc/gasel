package nc.ccas.gasel.agents.budget.params;

import static java.util.Arrays.asList;
import static nc.ccas.gasel.modelUtils.CommonQueries.resolveIds;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.reports.PeriodeProps;

public class BudgetParImpPublicFreqParams extends BudgetParams {

	private Set<Imputation> imputations;

	private Set<TypePublic> publics;

	private Set<StatutAide> statuts;

	public BudgetParImpPublicFreqParams() {
		super();
	}

	public BudgetParImpPublicFreqParams(PeriodeProps periode) {
		super(periode);
	}

	public void setStatuts(StatutAide... statuts) {
		setStatuts(Arrays.asList(statuts));
	}

	public void setStatuts(int... statuts) {
		setStatuts(resolveIds(StatutAide.class, statuts));
	}

	public void setImputations(Imputation... imputations) {
		setImputations(asList(imputations));
	}

	public void setImputations(int... imputations) {
		setImputations(resolveIds(Imputation.class, imputations));
	}

	public void setPublics(TypePublic... publics) {
		setPublics(Arrays.asList(publics));
	}

	public void setPublics(int... publics) {
		setPublics(CommonQueries.resolveIds(TypePublic.class, publics));
	}

	// -- get/set

	public Set<StatutAide> getStatuts() {
		return statuts;
	}

	public Set<Imputation> getImputations() {
		return imputations;
	}

	public Set<TypePublic> getPublics() {
		return publics;
	}

	public void setStatuts(Collection<StatutAide> statuts) {
		this.statuts = new HashSet<StatutAide>(statuts);
	}

	public void setImputations(Collection<Imputation> imputations) {
		this.imputations = new HashSet<Imputation>(imputations);
	}

	public void setPublics(Collection<TypePublic> publics) {
		this.publics = new HashSet<TypePublic>(publics);
	}

}
