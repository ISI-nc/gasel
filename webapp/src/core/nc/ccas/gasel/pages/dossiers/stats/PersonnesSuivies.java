package nc.ccas.gasel.pages.dossiers.stats;

import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.stats.DossierStats;
import nc.ccas.gasel.stats.TableauStat;

public abstract class PersonnesSuivies extends StatsSurPeriodePage {

	@Override
	protected TableauStat buildTableau() {
		return new TableauStat(Dossier.class, //
				DossierStats.origineSignalement(true), //
				DossierStats.quartier(), //
				// DossierStats.typeSuivi(true), // 
				DossierStats.situationFamiliale(true), //
				DossierStats.enfantsACharge(true) //
				// DossierStats.problematiques(true) //
		);
	}

}
