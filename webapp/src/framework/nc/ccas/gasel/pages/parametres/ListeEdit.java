package nc.ccas.gasel.pages.parametres;

import nc.ccas.gasel.EditPage;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.Relationship;
import org.apache.tapestry.annotations.InjectPage;

public abstract class ListeEdit extends EditPage<DataObject> {

	public ListeEdit() {
		super(DataObject.class);
	}

	@InjectPage("parametres/Listes")
	public abstract Listes getListesPage();

	public String getTitre() {
		if (getObject() == null) {
			return "Modification";
		}
		String entityName = getObject().getClass().getSimpleName();
		return getListesPage().getEnumName(entityName);
	}

	// (presque) duplicata de Listes

	public boolean getHasParent() {
		return parentRel() != null;
	}

	public String getParentEnum() {
		return parentEntity().getName();
	}

	private ObjEntity parentEntity() {
		return (ObjEntity) parentRel().getTargetEntity();
	}

	private Relationship parentRel() {
		return getObjectContext().getEntityResolver()
				.lookupObjEntity(getObject()).getRelationship("parent");
	}

}
