package nc.ccas.gasel.jwcs.core;

import nc.ccas.gasel.BasePage;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.annotations.Persist;

public abstract class Bloc extends BaseComponent {

	public abstract boolean getOuvertInitial();

	@Persist("workflow")
	public abstract Boolean getOuvertStored();

	public abstract void setOuvertStored(Boolean value);

	public boolean getOuvert() {
		if (getOuvertStored() == null) {
			return getOuvertInitial();
		}
		return getOuvertStored();
	}

	public void toggle() {
		setOuvertStored(!getOuvert());
		((BasePage) getPage()).redirect(); //getOuvert() ? getClientId() : null);
	}

}
