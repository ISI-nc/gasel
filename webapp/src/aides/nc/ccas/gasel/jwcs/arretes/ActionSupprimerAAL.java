package nc.ccas.gasel.jwcs.arretes;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.jwcs.core.tableau.Action;
import nc.ccas.gasel.model.aides.Arrete;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.pages.arretes.AjoutAidesLigne;
import nc.ccas.gasel.pages.arretes.Creation;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Parameter;

public abstract class ActionSupprimerAAL extends Action {

	@Override
	@InitialValue("listener:delete")
	public abstract IActionListener getAction();

	@Parameter
	public abstract Arrete getArrete();

	@Override
	@Parameter(defaultValue = "ognl:delete")
	public abstract IAsset getIcon();

	@Override
	@Parameter(defaultValue = "literal:Supprimer")
	public abstract Object getTitle();

	@Override
	@Parameter(defaultValue = "true")
	public abstract Object getConfirm();

	@Override
	@Parameter(defaultValue = "false")
	public abstract boolean getDisabled();

	@Asset("context:/images/delete.gif")
	public abstract IAsset getDelete();

	public void delete(IRequestCycle cycle, AjoutAidesLigne aal) {
		Arrete arrete = getArrete();
		for (Bon bon : aal.getBons()) {
			arrete.removeFromBonsValides(bon.getUsage());
		}
		BasePage page = (BasePage) getPage();
		if (page instanceof Creation) {
			((Creation) page).sauver();
		}
		page.redirect();
	}

}
