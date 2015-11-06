package nc.ccas.gasel.jwcs.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.Check;
import nc.ccas.gasel.CompositeHashMap;
import nc.ccas.gasel.Formats;
import nc.ccas.gasel.jwcs.core.tableau.AbstractAction;
import nc.ccas.gasel.jwcs.core.tableau.Cellule;
import nc.ccas.gasel.jwcs.core.tableau.ColonnesDynamiques;
import nc.ccas.gasel.jwcs.core.tableau.GroupeColonnes;
import nc.ccas.gasel.jwcs.core.tableau.GroupeDesc;
import nc.ccas.gasel.jwcs.core.tableau.ModeleColonne;
import nc.ccas.gasel.utils.CssStyleBuilder;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.form.Checkbox;
import org.apache.tapestry.form.ImageSubmit;
import org.apache.tapestry.form.Submit;
import org.apache.tapestry.services.DataSqueezer;

public abstract class Tableau extends AbstractFormComponent {

	public static final String SOURCE = "tableau_source";

	private List<GroupeDesc> _groupes;

	private List<ModeleColonne> _colonnes;

	private List<AbstractAction> _actions;

	private ArrayList<String> _titres;

	private List<?> _source;

	private List<Object> _range;

	private Boolean _showActions;

	public abstract int getLignesParPage();

	public abstract List<?> getSourceDirect();

	@Persist("workflow")
	@InitialValue("literal:1")
	public abstract int getPageNumber();

	public abstract void setPageNumber(int value);

	public abstract Object getValue();

	public abstract void setValue(Object object);

	public abstract Cellule getCellule();

	public abstract void setCellule(Cellule cellule);

	public abstract String getTitre();

	public List<?> getSource() {
		if (_source == null) {
			_source = getSourceDirect();
			if (_source == null) {
				throw new ApplicationRuntimeException(
						"The parameter source must not be null", getLocation(),
						new NullPointerException());
			}
		}
		return _source;
	}

	public boolean getShowActions() {
		if (_showActions == null) {
			_showActions = !getActions().isEmpty();
		}
		return _showActions;
	}

	public int getNbCols() {
		return (getShowSelection() ? 1 : 0) + getTitres().size()
				+ getActions().size();
	}

	public void showPage(IRequestCycle cycle, int pageNumber) {
		setPageNumber(pageNumber);
		((BasePage) getPage()).redirect(cycle);
	}

	public void showLastPage() {
		setPageNumber(getPageCount());
	}

	public int getRealPageNumber() {
		return Math.min(getPageNumber(), getPageCount());
	}

	public void setRealPageNumber(int value) {
		setPageNumber(Math.min(value, getPageCount()));
	}

	public int getPageCount() {
		int lpp = getLignesParPage();
		int sz = getSource().size();
		return (sz > 0 && sz % lpp == 0) ? sz / lpp : (sz / lpp + 1);
	}

	public List<GroupeDesc> getGroupes() {
		if (_groupes == null) {
			List<GroupeDesc> groupes = new ArrayList<GroupeDesc>();
			groupesAdd(groupes, getColonnes());
			boolean showHeader = false;
			for (GroupeDesc desc : groupes) {
				if (desc.getTitre() != null) {
					showHeader = true;
					break;
				}
			}
			_groupes = showHeader ? groupes : new LinkedList<GroupeDesc>();
		}
		return _groupes;
	}

	public static void groupesAdd(List<GroupeDesc> groupes,
			Collection<ModeleColonne> colonnes) {
		for (ModeleColonne modele : colonnes) {
			if (!modele.getVisible())
				continue;
			String titre;
			if (modele instanceof GroupeColonnes) {
				titre = ((GroupeColonnes) modele).getTitre();
			} else if (modele instanceof ColonnesDynamiques) {
				groupes.addAll(((ColonnesDynamiques) modele).getGroupes());
				continue;
			} else {
				titre = null;
			}
			groupes.add(new GroupeDesc(modele.getTitres().size(), titre));
		}
	}

	public List<ModeleColonne> getColonnes() {
		if (_colonnes == null) {
			_colonnes = componentsOfClass(ModeleColonne.class, this);
		}
		return _colonnes;
	}

	public List<AbstractAction> getActions() {
		if (_actions == null) {
			_actions = componentsOfClass(AbstractAction.class, this);
		}
		return _actions;
	}

	public List<?> getRange() {
		if (_range == null) {
			List<?> source = getSource();
			if (getLignesParPage() <= 0) {
				return source;
			}
			int currentPage = getRealPageNumber();
			int lignesParPage = getLignesParPage();

			int fromIndex = (currentPage - 1) * lignesParPage;
			int toIndex = Math.min(source.size(), currentPage * lignesParPage);
			_range = new ArrayList<Object>(lignesParPage);
			_range.addAll(source.subList(fromIndex, toIndex));
		}
		return _range;
	}

	public static <T> List<T> componentsOfClass(Class<T> clazz,
			AbstractComponent component) {
		return componentsOfClass(clazz, component, (Check<T>) null);
	}

	public static <T> List<T> componentsOfClass(Class<T> clazz,
			AbstractComponent component, Check<T> check) {
		List<T> retval = new LinkedList<T>();
		componentsOfClass(clazz, component, retval, check);
		return retval;
	}

	private static <T> void componentsOfClass(Class<T> clazz,
			AbstractComponent component, List<T> list, Check<T> check) {
		IRender[] body = component.getBody();
		if (body == null) {
			return;
		}
		for (IRender child : body) {
			if (child == null) {
				continue;
			}
			if (!clazz.isInstance(child)) {
				continue;
			}
			T t = clazz.cast(child);
			if (check != null && !check.check(t)) {
				continue;
			}
			list.add(t);
			// if (child instanceof AbstractComponent) {
			// componentsOfClass(clazz, (AbstractComponent) child, list);
			// }
		}
	}

	public List<Object> getActionParameters() {
		List<Object> objs = new LinkedList<Object>();
		objs.add(getValue());
		if (getAction().getParameters() != null) {
			for (Object o : getAction().getParameters()) {
				objs.add(o);
			}
		}
		return objs;
	}

	// ---------------------------------------------

	public abstract AbstractAction getAction();

	public abstract void setAction(AbstractAction action);

	public String getActionJs() {
		if (dontConfirm()) {
			return null;
		}
		String message = confirmMessage().replace("'", "\\'");
		return "return confirm('" + message + "')";
	}

	private boolean dontConfirm() {
		Object o = getAction().getConfirm();
		if (o instanceof Boolean) {
			return !((Boolean) o);
		} else {
			String s = (String) o;
			if ("false".equalsIgnoreCase(s)) {
				return true;
			}
			return false;
		}
	}

	private String confirmMessage() {
		Object o = getAction().getConfirm();
		if (o instanceof Boolean) {
			return getAction().getTitle() + " ?";
		} else {
			String s = (String) o;
			if ("true".equalsIgnoreCase(s)) {
				return getAction().getTitle() + " ?";
			}
			return s;
		}
	}

	public List<String> getTitres() {
		if (_titres == null) {
			_titres = new ArrayList<String>();
			for (ModeleColonne col : getColonnes()) {
				if (!col.getVisible())
					continue;
				_titres.addAll(col.getTitres());
			}
		}
		return _titres;
	}

	public List<Cellule> getValeurs() {
		List<Cellule> valeurs = new ArrayList<Cellule>();
		for (ModeleColonne col : getColonnes()) {
			if (!col.getVisible())
				continue;
			valeurs.addAll(col.getValeurs());
		}
		return valeurs;
	}

	public String getStyleCellule() {
		Cellule cell = getCellule();
		Object value = cell.getValue();
		CssStyleBuilder style = new CssStyleBuilder();
		if (value instanceof Number) {
			style.append("text-align:right").append("font-family:monospace");
		} else if (value instanceof Date) {
			style.append("text-align:center").append("font-family:monospace");
		}
		if (cell.isDottedTop()) {
			style.append("border-top:dotted 1px");
		}
		return style.toString();
	}

	// -----

	public abstract int getIdx();

	public boolean isLast() {
		return getIdx() == (getRange().size() - 1);
	}

	// ------------------------------------------------------------------------
	// Gestion de la sélection
	//

	@SuppressWarnings("rawtypes")
	public abstract Collection getSelection();

	public boolean getShowSelection() {
		return getSelection() != null;
	}

	public boolean getSelected() {
		return getSelection().contains(getValue());
	}

	@SuppressWarnings("unchecked")
	public void setSelected(boolean selected) {
		Object value = getValue();
		if (selected) {
			if (getSelection().contains(value))
				return;
			getSelection().add(value);
		} else {
			getSelection().remove(value);
		}
	}

	// ------------------------------------------------------------------------
	// Rendu (Java pur pour les perfs et le controle accru)
	//

	@Component(type = "core/Pager", bindings = { "page=pageNumber",
			"pageCount=pageCount", "async=false", // false... snif
			"updateComponents=ognl:{clientId}" })
	public abstract Pager getPager();

	@Component(type = "core/ServiceButton", bindings = { "label=literal:XLS",
			"service=literal:xls", "parameters={idPath,clientId}" })
	public abstract ServiceButton getXlsButton();

	@Component(bindings = { "value=selected" })
	public abstract Checkbox getCheckBox();

	@InjectObject("service:tapestry.data.DataSqueezer")
	public abstract DataSqueezer getDataSqueezer();

	public abstract Object getActionValue();

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		cache().getFrom(this);
		_showActions = null;
		_groupes = null;
		_colonnes = null;
		_actions = null;
		_titres = null;
		_actions = null;
		_source = null;
		_range = null;
	}

	@Override
	protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		if (needToStoreValues()) {
			storeValues();
		}
		renderCommon(writer, cycle);
	}

	private void storeValues() {
		IForm form = getForm();
		String name = form.getElementId(this);
		DataSqueezer squeezer = getDataSqueezer();
		for (Object o : getRange()) {
			String stringRep = squeezer.squeeze(o);
			form.addHiddenValue(name, stringRep);
		}
	}

	@Override
	protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		if (!getForm().isRewinding()) {
			return;
		}
		if (needToStoreValues()) {
			_range = loadValues(cycle);
		}
		renderCommon(writer, cycle);

		setValue(getActionValue());
	}

	private boolean needToStoreValues() {
		return getShowSelection() || getShowActions();
	}

	private List<Object> loadValues(IRequestCycle cycle) {
		IForm form = getForm();
		String name = form.getElementId(this);
		DataSqueezer squeezer = getDataSqueezer();

		String[] stringReps = cycle.getParameters(name);
		if (stringReps == null)
			return Collections.emptyList();

		List<Object> range = new ArrayList<Object>(stringReps.length);
		for (String s : stringReps) {
			Object o = squeezer.unsqueeze(s);
			range.add(o);
		}
		return range;
	}

	private void renderCommon(IMarkupWriter writer, IRequestCycle cycle) {
		writer.begin("div");
		renderIdAttribute(writer, cycle);

		// table class=data-table + paramètres informels
		writer.begin("table");
		writer.attribute("class", "data-table");
		renderInformalParameters(writer, cycle);

		// En-tête
		writer.begin("thead");
		renderHeadTitre(writer);
		renderHeadGroupes(writer);
		renderHeadColonnes(writer);
		writer.end(); // thead

		// Body
		writer.begin("tbody");
		renderRows(writer, cycle);
		writer.end(); // tbody

		// Footer
		writer.begin("tfoot");
		writer.begin("tr");
		{
			beginBigCell(writer, "td");
			{
				writer.begin("div");
				{
					writer.attribute("style", "float:right;");
					renderBody(writer, cycle);
					writer.printRaw("&nbsp;");
					getXlsButton().render(writer, cycle);
				}
				writer.end(); // div
				getPager().render(writer, cycle);
			}
			writer.end(); // td
		}
		writer.end(); // tr
		writer.end(); // tfoot

		//
		writer.end(); // table
		writer.end(); // div
	}

	private void renderHeadTitre(IMarkupWriter writer) {
		String titre = getTitre();
		if (titre != null) {
			writer.begin("tr");
			writer.begin("th");
			writer.attribute("class", "table-title");
			writer.attribute("colspan", getNbCols());
			writer.print(titre);
			writer.end(); // th
			writer.end(); // tr
		}
	}

	private void renderHeadGroupes(IMarkupWriter writer) {
		List<GroupeDesc> groupes = getGroupes();
		if (groupes != null && !groupes.isEmpty()) {
			writer.begin("tr");

			if (getShowSelection()) {
				// Première colonne pour la sélection
				renderEmptyTh(writer);
			}

			for (GroupeDesc groupe : groupes) {
				writer.begin("th");
				if (groupe.getLargeur() > 1) {
					writer.attribute("colspan", groupe.getLargeur());
				}
				writer.print(groupe.getTitre());
				writer.end();
			}

			if (getShowActions()) {
				// Dernière colonne pour les actions
				renderEmptyTh(writer);
			}

			writer.end(); // tr
		}
	}

	private void renderHeadColonnes(IMarkupWriter writer) {
		writer.begin("tr");

		if (getShowSelection()) {
			// Première colonne pour la sélection
			writer.begin("th");
			writer.beginEmpty("input");
			writer.attribute("type", "checkbox");
			writer.attribute("onchange", "tableauSelectAllChanged(this)");
			writer.end(); // th
		}

		for (String titre : getTitres()) {
			writer.begin("th");
			writer.print(titre);
			writer.end(); // th
		}

		if (getShowActions()) {
			// Dernière colonne pour les actions
			renderEmptyTh(writer);
		}

		writer.end(); // tr
	}

	private void renderEmptyTh(IMarkupWriter writer) {
		writer.begin("th");
		writer.beginEmpty("br");
		writer.end();
	}

	private void renderRows(IMarkupWriter writer, IRequestCycle cycle) {
		if (cycle.isRewinding() && !needToStoreValues())
			return;

		List<?> range = getRange();
		if (range.isEmpty()) {
			writer.begin("tr");
			beginBigCell(writer, "td");
			writer.print("Pas de données");
			writer.end(); // td
			writer.end(); // tr
			return;
		}

		// Lignes de données
		int i;
		for (i = 0; i < range.size(); i++) {
			Object value = range.get(i);
			setValue(value);
			renderRow(writer, cycle, i == range.size() - 1);
		}

		// Lignes pour combler la dernière page
		int minRows = Math.min(getSource().size(), getLignesParPage());
		for (; i < minRows; i++) {
			writer.begin("tr");
			writer.attribute("style", "border:none;");
			beginBigCell(writer, "td");
			writer.attribute("style", "border:none;");
			writer.printRaw("&nbsp;");
			writer.end();
			writer.end();
		}
	}

	private void beginBigCell(IMarkupWriter writer, String tag) {
		writer.begin(tag);
		if (getNbCols() > 1) {
			writer.attribute("colspan", getNbCols());
		}
	}

	private void renderRow(IMarkupWriter writer, IRequestCycle cycle,
			boolean last) {
		writer.begin("tr");
		if (last) {
			writer.attribute("class", "last");
		}

		if (getShowSelection()) {
			writer.begin("td");
			getCheckBox().render(writer, cycle);
			writer.end(); // td
		}

		if (!cycle.isRewinding()) {
			for (Cellule cellule : getValeurs()) {
				renderCellule(writer, cellule);
			}
		}

		renderActions(writer, cycle);

		writer.end(); // tr
	}

	private void renderCellule(IMarkupWriter writer, Cellule cellule) {
		setCellule(cellule);
		writer.begin("td");
		writer.attribute("style", getStyleCellule());
		if (cellule.isHighlight()) {
			writer.attribute("class", "highlight");
		}

		Object value = cellule.getValue();
		if (value == null) {
			writer.print("--");
		} else {
			String stringValue = cellule.getFormat().format(value);
			if (value instanceof Number) {
				// Ne pas couper les nombres
				stringValue = stringValue.replace(" ", Formats.NBSP);
			}
			writer.print(stringValue);
		}

		writer.end(); // td
	}

	private void renderActions(IMarkupWriter writer, IRequestCycle cycle) {
		if (getShowActions()) {
			writer.begin("td");
			for (AbstractAction action : getActions()) {
				renderAction(action, writer, cycle);
			}
			writer.end(); // td
		}
	}

	@Component(bindings = { "image=action.icon", "title=action.title",
			"alt=literal:", "listener=listener:setActionValue",
			"action=action.action", "parameters=actionParameters",
			"onclick=actionJs" })
	public abstract ImageSubmit getActionImage();

	@Component(bindings = { "value=action.title",
			"listener=listener:setActionValue", "action=action.action",
			"parameters=actionParameters", "onclick=actionJs",
			"class=literal:button" })
	public abstract Submit getActionSubmit();

	private void renderAction(AbstractAction action, IMarkupWriter writer,
			IRequestCycle cycle) {
		if (action.getDisabled())
			return;
		setAction(action);
		if (action.getIcon() == null) {
			getActionSubmit().render(writer, cycle);
		} else {
			getActionImage().render(writer, cycle);
		}
	}

	// ------------------------------------------------------- Gestion du cache

	public void useCache() {
		cache().setTo(this);
	}

	public void useCache(String clientId) {
		cache(clientId).setTo(this);
		if (_source == null) {
			throw new RuntimeException("Cache non trouvé (clientId: "
					+ clientId + ")");
		}
	}

	private CacheData cache() {
		return cache(getClientId());
	}

	private CacheData cache(String key) {
		BasePage page = (BasePage) getPage();
		CompositeHashMap cache = page.getCache();
		Object[] cacheKey = new Object[] { "core/Tableau", key };
		if (!cache.contains(cacheKey)) {
			cache.put(cacheKey, new CacheData());
		}
		return (CacheData) cache.get(cacheKey);
	}

	private static class CacheData implements Serializable {
		private static final long serialVersionUID = 3598621303637221089L;

		private List<?> source;

		public void getFrom(Tableau t) {
			source = t._source;
		}

		public void setTo(Tableau t) {
			t._source = source;
		}

	}

}