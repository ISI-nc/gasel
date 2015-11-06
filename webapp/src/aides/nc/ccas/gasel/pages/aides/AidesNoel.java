package nc.ccas.gasel.pages.aides;

import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.agents.aides.BonsNoel;
import nc.ccas.gasel.model.aides.Aide;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;

public abstract class AidesNoel extends BasePage {

	@Persist("workflow")
	public abstract List<Aide> getAides();

	public abstract void setAides(List<Aide> aides);

	@Override
	protected void prepareForRender(IRequestCycle cycle) {
		super.prepareForRender(cycle);
	}

	public void preparerAides() {
		setAides(BonsNoel.INSTANCE.createAidesNoel());
	}

	public void validerAides() {
		getObjectContext().commitChanges();
	}

}
