package nc.ccas.gasel;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.configuration.ModuleEntryPoint;
import nc.ccas.gasel.jwcs.Border;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.services.ModuleLister;
import nc.ccas.gasel.workflow.Workflow;
import nc.ccas.gasel.workflow.WorkflowEntry;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.Persistent;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.contrib.link.PopupLinkRenderer;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.Form;
import org.apache.tapestry.link.ILinkRenderer;

import com.asystan.common.tapestry.RedirectCallback;

public abstract class BasePage extends org.apache.tapestry.html.BasePage
		implements PageBeginRenderListener, PageEndRenderListener {

	public static final boolean PAGE_LOG = true;

	public void log(IComponent component, Object message) {
		if (!BasePage.PAGE_LOG)
			return;

		Logger.getLogger(getClass()).info(
				"[PAGE LOG] " + component.getExtendedId() + ": " + message);
	}

	private static final ThreadLocal<CompositeHashMap> REQUEST_CACHE = new ThreadLocal<CompositeHashMap>() {
		@Override
		protected CompositeHashMap initialValue() {
			return new CompositeHashMap();
		}
	};

	public static CompositeHashMap getRequestCache() {
		return REQUEST_CACHE.get();
	}

	public static void clearRequestCache() {
		REQUEST_CACHE.remove();
	}

	@InjectState("workflow")
	public abstract Workflow getWorkflow();

	@InjectState("login")
	public abstract LoginData getLogin();

	/* Actions standard */

	public void workflowOpen(IRequestCycle cycle, String pageName) {
		BasePage page = (BasePage) cycle.getPage(pageName);
		if (page instanceof EditPage) {
			((EditPage<?>) page).activate();
		} else {
			getWorkflow().getCurrentEntry().open(pageName, page.getTitre())
					.redirect(cycle);
		}
	}

	public void workflowOpen(String pageName) {
		workflowOpen(getRequestCycle(), pageName);
	}

	public void workflowClose(IRequestCycle cycle) {
		getWorkflow().close(cycle);
	}

	public void workflowClose() {
		workflowClose(getRequestCycle());
	}

	protected boolean dontRedirect = false;

	/**
	 * Ignore la prochaine tentative de redirection.
	 */
	public void dontRedirect() {
		dontRedirect = true;
	}

	public void redirect(IRequestCycle cycle) {
		redirect(cycle, null);
	}

	public void redirect(String anchor) {
		redirect(getRequestCycle(), anchor);
	}

	public void redirect(IRequestCycle cycle, String anchor) {
		if (dontRedirect) {
			dontRedirect = false;
		} else {
			getWorkflow().redirect(cycle, anchor);
		}
	}

	public void redirectTo(BasePage page) {
		WorkflowEntry we = getWorkflow().getCurrentEntry();
		we.setTitle(page.getTitre());
		we.setPageName(page.getPageName());
		redirect();
	}

	public void redirectTo(String pageName) {
		redirectTo((BasePage) getRequestCycle().getPage(pageName));
	}

	public void redirect() {
		redirect(getRequestCycle());
	}

	/**
	 * Prépare la page à l'ouverture. Après cet appel, on travaille dans un
	 * nouveau contexte.
	 * 
	 */
	public void prepareActivation() {
		Workflow wf = getWorkflow();
		WorkflowEntry entry = wf.getCurrentEntry().open(getPageName(),
				getTitre());
		wf.setCurrentEntry(entry);
		DataContext.bindThreadDataContext(getObjectContext());
	}

	/* Contrôle d'accès */

	@Override
	public void validate(IRequestCycle cycle) {
		super.validate(cycle);
		log(">>> Vérification des droits");
		// Pas de contrôle d'accès sur Home et AccessDenied
		if (unprotectedPage(getPageName())) {
			return;
		}
		// Login nécessaire
		if (!getLogin().isLoggedIn()) {
			throw new PageRedirectException("Home");
		}

		// Habilitation nécessaire
		if (!hasAccess(getPageName())) {
			throw new PageRedirectException("AccessDenied");
		}
		log("<<< Vérification des droits");
	}

	protected void log(Object message) {
		log(this, message);
	}

	public boolean hasAccess(String pageName) {
		if (unprotectedPage(pageName)) {
			return true;
		}
		return getLogin().hasAccess(pageName);
	}

	private boolean unprotectedPage(String pageName) {
		return "Home".equals(pageName) || "AccessDenied".equals(pageName);
	}

	// ------------------------------------------------------------------
	// Submit listeners support
	//

	private List<IActionListener> submitListeners = new LinkedList<IActionListener>();

	public void registerOnSubmit(IActionListener listener) {
		submitListeners.add(listener);
	}

	public void invokeSubmitListeners() {
		for (IActionListener listener : submitListeners) {
			listener.actionTriggered(this, getRequestCycle());
		}
	}

	/* -- */

	@InjectObject("service:gasel.ModuleLister")
	public abstract ModuleLister getModuleLister();

	public String getTitre() {
		ModuleEntryPoint ep = getModuleLister().lookupEntryPoint(getPageName());
		if (ep != null) {
			return ep.getTitre();
		}
		return getPageName();
	}

	public ICallback getCallback() {
		return RedirectCallback.createService("workflow", getRequestCycle(),
				getWorkflow().getCurrentEntry());
	}

	private boolean pageBeginRenderCalled = false;

	public void pageBeginRender(PageEvent event) {
		pageBeginRenderCalled = true;
		if (getCache() == null) {
			setCache(new CompositeHashMap());
		}
		IRequestCycle cycle = event.getRequestCycle();
		DataContext.bindThreadDataContext(getObjectContext());
		if (cycle.isRewinding()) {
			// Fix: pas appelé par tapestry dans ce cas
			prepareForRender(cycle);
		}
	}

	@Override
	protected void prepareForRender(IRequestCycle cycle) {
		log("--> prepareForRender [rewinding: " + cycle.isRewinding() + "]");
		if (!pageBeginRenderCalled)
			throw new RuntimeException("Bad overload of "
					+ "pageBeginRender(event): "
					+ "you must call super.pageBeginRender(event)");

		super.prepareForRender(cycle);
		WorkflowEntry entry = getWorkflow().getCurrentEntry();
		DataContext.bindThreadDataContext(entry.getObjectContext());
		if (getPageName().equals(entry.getPageName())) {
			entry.setTitle(getTitre());
		}
	}

	@Override
	public final void pageEndRender(PageEvent event) {
		super.pageEndRender(event);
		IRequestCycle cycle = event.getRequestCycle();
		if (cycle.isRewinding()) {
			// Fix: pas appelé par tapestry dans ce cas
			cleanupAfterRender(cycle);
		}
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		log("--> cleanupAfterRender [rewinding: " + cycle.isRewinding() + "]");
		super.cleanupAfterRender(cycle);

		pageBeginRenderCalled = false;
		dontRedirect = false;
		sql.query().records.clear();
		submitListeners.clear();

		clearRequestCache();

		// if (MairieDSF.CONNECTION.get() != null) {
		// try {
		// MairieDSF.CONNECTION.get().realClose();
		// } catch (SQLException e) {
		// throw new RuntimeException(e);
		// }
		// }
	}

	public DataContext getObjectContext() {
		return getWorkflow().getCurrentEntry().getObjectContext();
	}

	public void setDataContext(DataContext context) {
		getWorkflow().getCurrentEntry().setDataContext(context);
		DataContext.bindThreadDataContext(context);
	}

	@Deprecated
	public DateFormat getDateFormat() {
		return Formats.DATE_FORMAT;
	}

	@Deprecated
	public DateFormat getTimeFormat() {
		return Formats.TIME_FORMAT;
	}

	@Deprecated
	public DateFormat getDateTimeFormat() {
		return Formats.DATE_TIME_FORMAT;
	}

	private Formats format = Formats.INSTANCE;

	public Formats getFormat() {
		return format;
	}

	/* Accès aux composants essentiels ------------------------------------- */

	public Border getBorderComponent() {
		for (Object o : getComponents().values()) {
			if (o instanceof Border)
				return (Border) o;
		}
		throw new RuntimeException("Page invalide : pas de composant Border.");
	}

	public Form getForm() {
		for (Object o : getBorderComponent().getComponents().values()) {
			if (o instanceof Form)
				return (Form) o;
		}
		throw new RuntimeException("Border invalide : pas de composant Form.");
	}

	/* Utiles -------------------------------------------------------------- */

	public String objectStatus(DataObject o) {
		switch (o.getPersistenceState()) {
		case PersistenceState.NEW:
			return " (nouveau)";
		case PersistenceState.DELETED:
			return " (supprimé)";
			// case PersistenceState.MODIFIED: return " (modifié)";
		default:
			return "";
		}
	}

	/* Extensions ---------------------------------------------------------- */

	public final BasePageSql sql = new BasePageSql(this);

	public final BasePagePsm psm = new BasePagePsm(this);

	public final BasePageDates dates = BasePageDates.INSTANCE;

	public final BasePageLists lists = new BasePageLists();

	public final BasePageWorkflow wf = new BasePageWorkflow(this);

	public <T> BasePageSort<T> sort(Collection<T> collection) {
		return new BasePageSort<T>(collection);
	}

	public <T> BasePageSort<T> sort() {
		return new BasePageSort<T>();
	}

	public <T> BasePageSort<T> getSort() {
		return sort();
	}

	@Persist("workflow")
	public abstract CompositeHashMap getCache();

	public abstract void setCache(CompositeHashMap cache);

	protected void triggerModified(ModifListener object) {
		object.modified(getLogin().getUser(), new Date());
	}

	/* ----------------- */

	public boolean isNew(DataObject o) {
		return o.getPersistenceState() == PersistenceState.NEW;
	}

	public <T extends DataObject> T createDataObject(Class<T> clazz) {
		return clazz.cast(getObjectContext().newObject(clazz));
	}

	public <T extends DataObject> T objectForPk(Class<T> clazz, int pk) {
		return clazz.cast(DataObjectUtils.objectForPK(getObjectContext(), clazz,
				pk));
	}

	protected <E extends Persistent> E localObject(E object) {
		return (E) getObjectContext().localObject(object.getObjectId(), object);
	}

	public boolean getIsTestMode() {
		return Utils.isTestMode();
	}

	public <T extends DataObject> T ensureDataContext(T obj) {
		return Utils.ensureDataContext(obj, getObjectContext());
	}

	public ILinkRenderer getPopupLink() {
		return new PopupLinkRenderer(RandomStringUtils.randomAlphanumeric(10),
				"directories=no,location=no,menubar=no,personalbar=no,"
						+ "toolbar=no,titlebar=no");
	}

}
