package nc.ccas.gasel.pages.dossiers.stats;

import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.stats.DossierStats;
import nc.ccas.gasel.stats.TableauStat;

import org.apache.tapestry.annotations.Persist;

public abstract class PersonnesPaio extends StatsSurPeriodePage {

	@Override
	protected TableauStat buildTableau() {
		TableauStat ts = new TableauStat(Dossier.class, //
				DossierStats.origineSignalement(true), //
				DossierStats.quartier(), //
				DossierStats.situationFamiliale(true), //
				DossierStats.enfantsACharge(true), //
				DossierStats.situationProf(true), //
				DossierStats.revenus(this, true), //
				DossierStats.problematiques(true) //
		);
		ts.addToSelector(DossierStats.critereResteDuMonde());
		return ts;
	}
	
	@Persist("workflow")
	public abstract boolean getGroupSitFam();

}
