package nc.ccas.gasel.agents.budget;

import java.util.Map;
import java.util.Set;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.agents.budget.impl.DossiersPersonnesMontantParPublicImpl;
import nc.ccas.gasel.agents.budget.result.DossiersPersonnesMontant;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.reports.PeriodeProps;

public interface DossiersPersonnesMontantParPublic {

	public static final DossiersPersonnesMontantParPublic INSTANCE = new DossiersPersonnesMontantParPublicImpl();

	public Map<TypePublic, DossiersPersonnesMontant> getDossierPersonnesMontantParPublic(
			BasePageSql sql, PeriodeProps periode, Set<TypePublic> publics,
			int imputation);

	public Map<TypePublic, DossiersPersonnesMontant> getDossierPersonnesMontantParPublic(
			BasePageSql sql, PeriodeProps periode, Set<TypePublic> publics,
			Imputation imputation);

}
