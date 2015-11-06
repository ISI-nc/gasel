package nc.ccas.gasel.agents.budget;

import java.util.Collection;

import nc.ccas.gasel.agents.budget.impl.SuiviParPublicNatureImpl;
import nc.ccas.gasel.agents.budget.result.DossiersPersonnesMontant;
import nc.ccas.gasel.agents.budget.result.PublicNatureMap;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.reports.PeriodeProps;

public interface SuiviParPublicNature {

	public static final SuiviParPublicNature INSTANCE = new SuiviParPublicNatureImpl();

	public PublicNatureMap<DossiersPersonnesMontant> getSuiviParPublicNature(
			PeriodeProps periode, Collection<TypePublic> publics, int imputation);

	public PublicNatureMap<DossiersPersonnesMontant> getSuiviParPublicNature(
			PeriodeProps periode, Collection<TypePublic> publics,
			Imputation imputation);

}
