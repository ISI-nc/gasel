package nc.ccas.gasel.pages.parametres;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.core.ConstDouble;
import nc.ccas.gasel.model.core.ConstInteger;
import nc.ccas.gasel.model.core.ConstString;
import nc.ccas.gasel.model.core.Constante;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.ValidationDelegate;

public abstract class Constantes extends BasePage {

	protected static final Map<Class<? extends DataObject>, String> MANAGED_TYPES = new HashMap<Class<? extends DataObject>, String>();
	static {
		MANAGED_TYPES.put(ConstInteger.class, "Entier");
		MANAGED_TYPES.put(ConstDouble.class, "Réel");
		MANAGED_TYPES.put(ConstString.class, "Texte");
	}

	public void enregistrer() {
		ValidationDelegate delegate = (ValidationDelegate) getComponent(
				"border").getBeans().getBean("delegate");
		if (delegate.getHasErrors()) {
			return;
		}
		getObjectContext().commitChanges();
		Constante.clearCache();
		workflowClose();
	}

	private List<DataObject> _constantes;

	public List<DataObject> getConstantes() {
		if (_constantes == null) {
			_constantes = new ArrayList<DataObject>();
			for (Class<? extends DataObject> constClass : MANAGED_TYPES
					.keySet()) {
				_constantes.addAll(allObjects(constClass));
			}
			Collections.sort(_constantes, new Comparator<DataObject>() {
				public int compare(DataObject o1, DataObject o2) {
					String lib1 = (String) o1.readProperty("libelle");
					String lib2 = (String) o2.readProperty("libelle");
					return lib1.compareTo(lib2);
				}
			});
		}

		return _constantes;
	}

	public abstract DataObject getConstante();

	public String getConstType() {
		return MANAGED_TYPES.get(getConstante().getClass());
	}

	@SuppressWarnings("unchecked")
	protected <T extends DataObject> List<T> allObjects(Class<T> clazz) {
		return getObjectContext().performQuery(new SelectQuery(clazz));
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_constantes = null;
	}

}

class ConstFacade {

	private final String type;

	private final DataObject constante;

	public ConstFacade(DataObject constante) {
		this.constante = constante;
		type = Constantes.MANAGED_TYPES.get(constante.getClass());
		if (type == null) {
			throw new RuntimeException("Type de constante non géré : "
					+ constante.getClass().getName());
		}
	}

	public DataObject getConstante() {
		return constante;
	}

	public String getType() {
		return type;
	}

}