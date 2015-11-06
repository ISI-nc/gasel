package nc.ccas.gasel.pages.parametres;

import static com.asystan.common.cayenne.QueryFactory.createAnd;
import static com.asystan.common.cayenne.QueryFactory.createEquals;

import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.jwcs.core.SideBar;
import nc.ccas.gasel.jwcs.core.Tableau;
import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.Relationship;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.query.SelectQuery;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.annotations.Persist;

import com.asystan.common.cayenne.QueryFactory;

public abstract class Listes extends BasePage {

	private static final String DEFAULT_EDIT_PAGE = "parametres/ListeEdit";

	public abstract List<Category> getEnums();

	@Persist("workflow")
	public abstract EnumDesc getListe();

	@Override
	public String getTitre() {
		if (getListe() == null) {
			return "Listes";
		}
		return "Liste : " + getListe().title;
	}

	public abstract DataObject getNewValueParent();

	public abstract void setNewValueParent(DataObject parent);

	public abstract String getNewValue();

	public abstract void setNewValue(String value);

	public abstract void setAjouterError(String error);

	public void ajouterValeur() {
		if (getNewValue() == null) {
			setAjouterError("Pas de libellé !");
			return;
		}
		if (getHasParent() && getNewValueParent() == null) {
			setAjouterError("Pas de parent !");
			return;
		}
		DataObject newValue = (DataObject) getObjectContext().createAndRegisterNewObject(
				getListe().entityName);
		PropertyUtils.write(newValue, "actif", true);
		PropertyUtils.write(newValue, "locked", false);
		PropertyUtils.write(newValue, "libelle", getNewValue());
		if (getHasParent()) {
			DataObject parent = getNewValueParent();
			PropertyUtils.write(newValue, "parent", parent);
			setFilter(parent);
		}
		try {
			getObjectContext().commitChanges();
		} catch (RuntimeException e) {
			getObjectContext().rollbackChanges();
			throw e;
		}
		setNewValue(null);
		Tableau tableau = (Tableau) getComponent("liste");
		tableau.showLastPage();

		redirect();
	}

	public Category category(String name) {
		return new Category(name);
	}

	@SuppressWarnings("unchecked")
	public List<? extends Persistent> getValues() {
		// Qualifier
		Expression expr = QueryFactory.createTrue("actif");

		Persistent filter = getFilter();
		if (filter != null
				&& (parentEntity() == null || !isValidParent(filter))) {
			filter = null;
		}

		if (filter != null) {
			expr = createAnd(expr, createEquals("parent", getFilter()));
		}

		// Requête
		SelectQuery q = new SelectQuery(getListe().entityName, expr);
		if (getHasParent()) {
			q.addOrdering(new Ordering("parent.libelle", true));
		}
		q.addOrdering(new Ordering("libelle", true));

		// Exécution
		return getObjectContext().performQuery(q);
	}

	private boolean isValidParent(Persistent filter) {
		return parentEntity().getJavaClass().isInstance(filter);
	}

	public boolean getHasParent() {
		return parentRel() != null;
	}

	@Persist("workflow")
	public abstract DataObject getCurrentParent();

	public abstract void setCurrentParent(DataObject parent);

	public String getParentEnum() {
		return parentEntity().getName();
	}

	private ObjEntity parentEntity() {
		Relationship rel = parentRel();
		if (rel == null)
			return null;
		return (ObjEntity) rel.getTargetEntity();
	}

	private Relationship parentRel() {
		EnumDesc liste = getListe();
		ObjEntity entity = liste.getObjEntity();
		return entity.getRelationship("parent");
	}

	public String getDefaultEditPage() {
		return DEFAULT_EDIT_PAGE;
	}

	public static class Category extends SideBar.Category<EnumDesc> {

		public Category(String title) {
			super(title);
		}

		public Category add(String entityName, String title) {
			return add(entityName, title, DEFAULT_EDIT_PAGE);
		}

		public Category add(String entityName, String title,
				String customEditPage) {
			values.add(new EnumDesc(entityName, title, customEditPage));
			return this;
		}
	}

	public static class EnumDesc extends SideBar.Value {
		private static final long serialVersionUID = -5475324355389558093L;

		public final String entityName;

		public final String customEditPage;

		public EnumDesc(String entityName, String title, String customEditPage) {
			super(title);
			this.entityName = entityName;
			this.customEditPage = customEditPage;
		}

		public ObjEntity getObjEntity() {
			return CayenneUtils.entityResolver().getObjEntity(entityName);
		}

	}

	public String getEnumName(String entityName) {
		for (Category cat : getEnums()) {
			for (EnumDesc desc : cat.values) {
				if (!desc.entityName.equals(entityName))
					continue;
				return desc.title;
			}
		}
		return null;
	}

	// ----------------------
	// Gestion du filtre
	//

	@Persist("workflow")
	public abstract Persistent getFilter();

	public abstract void setFilter(Persistent filter);

}