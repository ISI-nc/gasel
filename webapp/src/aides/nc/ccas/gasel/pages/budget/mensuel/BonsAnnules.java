package nc.ccas.gasel.pages.budget.mensuel;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;

public abstract class BonsAnnules extends BasePage {

	public void desannulerBon(Bon bon) {
		if (bon == null)
			return;

		EtatBon etat = sql.query().byId(EtatBon.class, EtatBon.EDITE);
		bon.setEtat(etat, false);
		bon.getDataContext().commitChanges();
		
		redirect();
	}

}
