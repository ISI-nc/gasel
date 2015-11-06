package nc.ccas.gasel.jwcs.core.edit;

import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.ObjectCallback;
import nc.ccas.gasel.ObjectCallbackPage;
import nc.ccas.gasel.ObjectPage;

import org.apache.cayenne.DataObject;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;

public abstract class ToOne extends BaseComponent {

	public abstract String getSearchPage();

	public abstract String getViewPage();

	public abstract DataObject getValue();

	public abstract void setValue(DataObject value);

	public abstract IActionListener getOnSet();

	@SuppressWarnings("unchecked")
	public <T extends DataObject> void recherche() {
		ObjectPage<T> myPage = (ObjectPage) getPage();
		IRequestCycle cycle = myPage.getRequestCycle();
		ObjectCallbackPage<T> searchPage = (ObjectCallbackPage) cycle
				.getPage(getSearchPage());
		searchPage.activate(new Callback(myPage, getIdPath()));
	}

	@SuppressWarnings("unchecked")
	public <T extends DataObject> void details(IRequestCycle cycle) {
		ObjectPage<T> viewPage = (ObjectPage<T>) cycle.getPage(getViewPage());
		viewPage.open((T) getValue());
	}

	public void delete() {
		setValue(null);
		((BasePage) getPage()).redirect();
	}

}

class Callback extends ObjectCallback<ObjectPage<?>, DataObject> {
	private static final long serialVersionUID = -570683597711172102L;

	private final String _idPath;

	public Callback(ObjectPage<?> page, String idPath) {
		super(page);
		_idPath = idPath;
	}

	@Override
	public void performCallback(ObjectPage<?> page, DataObject o) {
		IComponent comp = page;
		for (String pathElt : _idPath.split("\\.")) {
			comp = comp.getComponent(pathElt);
		}
		ToOne toOne = (ToOne) comp;
		toOne.setValue(o);
		IActionListener onSet = toOne.getOnSet();
		if (onSet != null) {
			onSet.actionTriggered(toOne, page.getRequestCycle());
		}
	}
}