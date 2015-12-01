package nc.ccas.gasel.jwcs.core;

import nc.ccas.gasel.EditPage;

import org.apache.cayenne.DataObject;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;

public abstract class Form extends BaseComponent {

	public void annuler() {
		page().annuler();
	}

	public void enregistrer() {
		page().enregistrer();
	}

	public void enregistrerSansFermer() {
		page().enregistrerSansFermer();
	}

	@SuppressWarnings("unchecked")
	private <T extends DataObject> EditPage<T> page() {
		return (EditPage<T>) getPage();
	}

	@Override
	protected void prepareForRender(IRequestCycle cycle) {
		page().getValidator().validate(page().getObject());
	}

}
