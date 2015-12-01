package nc.ccas.gasel.jwcs.core;

import nc.ccas.gasel.ObjectCallback;
import nc.ccas.gasel.ObjectCallbackPage;

import org.apache.cayenne.DataObject;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.Submit;

public abstract class BoutonCreer extends AbstractComponent {

	@Component(id = "creer", type = "Submit", inheritInformalParameters = true, bindings = {
			"action=listener:creer", "value=ognl:value", "class=cssClass" })
	public abstract Submit getCreerSubmit();

	@Parameter(required = true)
	public abstract String getPageCreation();

	@Parameter(defaultValue = "literal:Créer")
	public abstract String getValue();

	@Parameter(defaultValue = "true")
	public abstract boolean getAutoClose();

	@Parameter(name = "literal:class", defaultValue = "literal:button")
	public abstract String getCssClass();

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		getCreerSubmit().render(writer, cycle);
	}

	@SuppressWarnings("unchecked")
	public void creer(IRequestCycle cycle) {
		ObjectCallbackPage<? extends DataObject> page = (ObjectCallbackPage) cycle
				.getPage(getPageCreation());
		if (getAutoClose() && getPage() instanceof ObjectCallbackPage<?>) {
			page.activate(new Callback<DataObject>(getPage().getPageName()));
		} else {
			page.activate(null);
		}
	}

}

class Callback<T extends DataObject> extends
		ObjectCallback<ObjectCallbackPage<T>, T> {
	private static final long serialVersionUID = -510531561146792242L;

	public Callback(String pageName) {
		super(pageName);
	}

	@Override
	public void performCallback(ObjectCallbackPage<T> page, T o) {
		page.success(o);
	}

}