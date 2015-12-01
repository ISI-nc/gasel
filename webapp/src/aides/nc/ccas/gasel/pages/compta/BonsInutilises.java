package nc.ccas.gasel.pages.compta;

import java.text.NumberFormat;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.Formats;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;

public abstract class BonsInutilises extends BasePage {

	public void annulerBon(Bon bon) {
		if (bon == null)
			return;

		EtatBon annule = sql.query().byId(EtatBon.class, EtatBon.ANNULE);
		bon.setEtat(annule);
		getObjectContext().commitChanges();
		redirect();
	}

	public NumberFormat getFormatNumeroDossier() {
		return Formats.NUMERO_DOSSIER;
	}

}