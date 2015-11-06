package nc.ccas.gasel.pages.habilitations;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import nc.ccas.gasel.ApplyWriteAccess;
import nc.ccas.gasel.Check;
import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.Pages;
import nc.ccas.gasel.model.core.AccesPage;
import nc.ccas.gasel.model.core.AppPage;
import nc.ccas.gasel.model.core.Groupe;
import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageEvent;

public abstract class GroupeEdit extends EditPage<Groupe> {

	private Map<String, AppPage> _pages;

	@Persist("workflow")
	public abstract String getCategory();

	public abstract void setCategory(String category);

	public GroupeEdit() {
		super(Groupe.class);
	}

	@Override
	public String getTitre() {
		if (getObject() == null)
			return null;
		return "Groupe " + getObject().getLibelle();
	}

	public abstract String getIdPage();

	public Set<String> getPages() {
		return new TreeSet<String>(lists.filter(Pages.PAGES.keySet(),
				new Check<String>() {
					public boolean check(String value) {
						return splitCat(value)[0].equals(getCategory());
					}
				}));
	}

	public String getDescriptionPage() {
		return Pages.PAGES.get(getIdPage());
	}

	public AccesPage getAcces() {
		AppPage page = getAppPage();

		// Recherche de l'accès correspondant à la page
		AccesPage acces = findAcces(page);

		if (acces == null) {
			throw new RuntimeException("Acces non trouvé pour "
					+ page.getPage());
		}

		return acces;
	}

	private AccesPage findAcces(AppPage page) {
		Groupe groupe = getObject();
		for (AccesPage acces : groupe.getAccesPage()) {
			if (acces.getPage().equals(page)) {
				return acces;
			}
		}
		return null;
	}

	private Map<String, AppPage> getPagesEnBase() {
		if (_pages == null) {
			_pages = new TreeMap<String, AppPage>();
			for (AppPage page : sql.query().all(AppPage.class)) {
				_pages.put(page.getPage(), page);
			}
		}
		return _pages;
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);

		// Forcer une catégorie
		if (getCategory() == null) {
			setCategory(getCategories().iterator().next());
		}

		// Ajouter les pages manquantes en base
		Map<String, AppPage> pages = getPagesEnBase();
		for (String pageId : Pages.PAGES.keySet()) {
			if (pages.containsKey(pageId))
				continue;

			synchronized (AppPage.class) {
				_pages = null;
				pages = getPagesEnBase();
				AppPage page = pages.get(pageId);
				if (page != null)
					continue;

				DataContext context = CayenneUtils.createDataContext();
				page = (AppPage) context.newObject(AppPage.class);
				page.setPage(pageId);
				context.commitChanges();
				page = localObject(page);
			}
		}

		// Ajouter les accès manquants
		Groupe groupe = getObject();
		ObjectContext context = groupe.getObjectContext();
		for (AppPage page : getPagesEnBase().values()) {
			AccesPage acces = findAcces(page);
			if (acces != null)
				continue;

			acces = (AccesPage) context.newObject(AccesPage.class);
			acces.setGroupe(groupe);
			acces.setPage((AppPage) context.localObject(page.getObjectId(), page));
			acces.setRead(false);
			acces.setWrite(false);
		}
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_pages = null;
	}

	private AppPage getAppPage() {
		// Préchargement des pages
		AppPage page = getPagesEnBase().get(getIdPage());
		if (page == null) {
			throw new RuntimeException("Page non trouvée: " + getIdPage());
		}
		return page;
	}

	public boolean isEditPage() {
		IPage targetPage = getRequestCycle().getPage(getIdPage());
		// return targetPage instanceof EditPage<?>;
		return targetPage.getClass().getAnnotation(ApplyWriteAccess.class) != null;
	}

	public Set<String> getCategories() {
		Set<String> retval = new TreeSet<String>();
		for (String page : Pages.PAGES.keySet()) {
			retval.add(splitCat(page)[0]);
		}
		return retval;
	}

	private String[] splitCat(String page) {
		int idx = page.lastIndexOf("/");
		if (idx == -1)
			return new String[] { "", page };
		return new String[] { page.substring(0, idx), page.substring(idx + 1) };
	}

}
