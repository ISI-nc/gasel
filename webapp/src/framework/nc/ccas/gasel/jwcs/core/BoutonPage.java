package nc.ccas.gasel.jwcs.core;

import static org.apache.cayenne.access.DataContext.getThreadDataContext;

import java.util.Map;
import java.util.Map.Entry;

import nc.ccas.gasel.BasePage;

import org.apache.cayenne.Persistent;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.Submit;

public abstract class BoutonPage extends AbstractComponent {

	@Component(type = "Submit", inheritInformalParameters = true, bindings = {
			"id=clientId", "action=listener:lien", "class=cssClass",
			"value=value", "disabled=disabled" })
	public abstract Submit getSubmit();

	@Parameter(required = true)
	public abstract String getValue();

	@Parameter(defaultValue = "false")
	public abstract String getDisabled();

	@Parameter(name = "page", required = true)
	public abstract String getPageName();

	@Parameter(name = "class", defaultValue = "literal:button")
	public abstract String getCssClass();

	@Parameter
	public abstract Map<String, Object> getContext();

	public void lien(IRequestCycle cycle) {
		BasePage page = (BasePage) cycle.getPage(getPageName());
		page.prepareActivation();
		if (getContext() != null) {
			for (Entry<String, Object> entry : getContext().entrySet()) {
				String param = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof Persistent) {
					value = getThreadDataContext().localObject(
							((Persistent) value).getObjectId(),
							(Persistent) value);
				}
				PropertyUtils.write(page, param, value);
			}
		}
		page.redirect();
	}

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		getSubmit().render(writer, cycle);
	}

}
