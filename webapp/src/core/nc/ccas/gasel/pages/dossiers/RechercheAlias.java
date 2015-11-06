package nc.ccas.gasel.pages.dossiers;

import nc.ccas.gasel.BasePage;

import org.apache.cayenne.DataObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.event.PageEvent;

public abstract class RechercheAlias extends BasePage {

	private final String aspect;

	@InjectPage("dossiers/Recherche")
	public abstract Recherche getPageRecherche();

	@Override
	public void pageBeginRender(PageEvent event) {
		Recherche page = getPageRecherche();
		page.setAspect(aspect);
		page.setFiltreAspect(true);
		redirectTo(page);
	}

	public RechercheAlias(String aspect) {
		this.aspect = aspect;
	}

	public RechercheAlias(Class<? extends DataObject> aspectClass) {
		this(aspectClass.getSimpleName());
	}

}
