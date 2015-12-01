package nc.ccas.gasel.jwcs.core;

import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.event.PageEndRenderListener;

public abstract class Onglet extends Block implements PageEndRenderListener {

	@Parameter(required = true)
	public abstract String getTitre();

}
