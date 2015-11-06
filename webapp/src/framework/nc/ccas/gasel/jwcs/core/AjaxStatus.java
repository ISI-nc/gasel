package nc.ccas.gasel.jwcs.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.annotations.InjectScript;

public abstract class AjaxStatus extends AbstractComponent {

	@InjectScript("AjaxStatus.script")
	public abstract IScript getScript();

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		// add the script
		if (!cycle.isRewinding()) {
			PageRenderSupport pageRenderSupport = TapestryUtils
					.getPageRenderSupport(cycle, this);
			Map<?, ?> symbols = new HashMap<Object, Object>();
			getScript().execute(this, cycle, pageRenderSupport, symbols);
		}
	}

}
