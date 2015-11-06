package nc.ccas.gasel.pages.pe;

import nc.ccas.gasel.BasePage;

import org.apache.tapestry.IRequestCycle;

public abstract class CreationAM extends BasePage {

	@Override
	protected void prepareForRender(IRequestCycle cycle) {
		redirectTo("dossiers/Edition");
		super.prepareForRender(cycle);
	}

}
