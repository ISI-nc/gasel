package nc.ccas.gasel.jwcs.core.edit;

import java.util.ArrayList;
import java.util.List;

import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.validator.GaselValidator;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;
import org.apache.cayenne.validation.ValidationException;
import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

public abstract class SubForm extends BaseComponent implements
		PageBeginRenderListener {

	/**
	 * Objet source (conteneur de la liste).
	 */
	@Parameter(required = true)
	public abstract DataObject getSource();

	/**
	 * Le nom de la liste dans laquelle ajouter l'objet <code>value</code>.
	 */
	@Parameter(required = true)
	public abstract String getListe();

	/**
	 * La valeur en cours de modification.
	 */
	@Parameter(required = true)
	public abstract DataObject getValue();

	public abstract void setValue(DataObject value);

	/**
	 * Valeur à définir "source" peut être null au rendu de la page (cas
	 * complexes comme l'ajout de ressources/charges depuis la page dossier).
	 */
	@Parameter(name = "type")
	public abstract String getValueType();

	/**
	 * La méthode appelée après l'appel.
	 */
	@Parameter(defaultValue = "listener:redirect")
	public abstract IActionListener getOnadd();

	/**
	 * La méthode appelée lors de l'initialisation d'un objet.
	 */
	@Parameter
	public abstract IActionListener getOninit();

	// ----------------------------------------------------- Fin des paramètres

	@Persist("workflow")
	public abstract DataObject getObjectValue();

	public abstract void setObjectValue(DataObject object);

	public abstract List<String> getErrors();

	public abstract void setErrors(List<String> errors);

	private DataContext _parentContext;

	@SuppressWarnings("unchecked")
	public <T extends DataObject> void ajouter(IRequestCycle cycle) {
		T obj = (T) getObjectValue();
		Class<T> objClass = (Class<T>) obj.getClass();
		ObjectContext dc = dataContext();

		String liste = getListe();
		// String objToListeRel = getRel().getReverseRelationship().getName();

		// Ajout à la source
		DataObject source = getSource();
		DataObject localSource = (DataObject) dc.localObject(source.getObjectId(), null);
		// obj.setToOneTarget(objToListeRel, localSource, true);
		localSource.addToManyTarget(liste, obj, true);

		GaselValidator<T> validator = GaselValidator.validatorFor(objClass);
		validator.validate(obj);
		// obj.setToOneTarget(objToListeRel, null, true);
		localSource.removeToManyTarget(liste, obj, true);

		List<String> errors = validator.getErrors();
		setErrors(errors);

		if (!errors.isEmpty()) {
			return;
		}

		// obj.setToOneTarget(objToListeRel, localSource, true);
		if (localSource.getPersistenceState() == PersistenceState.MODIFIED) {
			localSource.setPersistenceState(PersistenceState.HOLLOW);
		}
		localSource.addToManyTarget(liste, obj, true);
		try {
			dataContext().commitChangesToParent();
		} catch (ValidationException ex) {
			localSource.removeToManyTarget(liste, obj, true);
			setErrors(translateErrors(ex));
			return;
		}

		setObjectValue(null);

		if (getOnadd() != null) {
			getOnadd().actionTriggered(this, cycle);
		}
	}

	private List<String> translateErrors(ValidationException ex) {
		return translateErrors(ex.getValidationResult());
	}

	private List<String> translateErrors(ValidationResult validationResult) {
		List<String> errors = new ArrayList<String>(validationResult
				.getFailures().size());
		for (Object o : validationResult.getFailures()) {
			ValidationFailure vf = (ValidationFailure) o;
			errors.add("! " + vf.getSource() + " : " + vf.getDescription());
		}
		return errors;
	}

	public void redirect() {
		((BasePage) getPage()).redirect();
	}

	// -----

	@Override
	protected void prepareForRender(IRequestCycle cycle) {
		super.prepareForRender(cycle);

		// Vérification des paramètres
		if (getValueType() == null) {
			if (getRel() == null)
				throw new ApplicationRuntimeException("Pas de relation "
						+ getSource().getClass().getSimpleName() + "."
						+ getListe(), getLocation(), null);
			if (!getRel().isToMany())
				throw new ApplicationRuntimeException("La relation "
						+ getSource().getClass().getSimpleName() + "."
						+ getListe() + " n'est pas un toMany.", getLocation(),
						null);
		}

		// Push du contexte parent et activation du sous-contexte (notre
		// contexte de travail).
		_parentContext = DataContext.getThreadDataContext();
		DataContext.bindThreadDataContext(dataContext());
	}

	public void pageBeginRender(PageEvent event) {
		// Gérer les prérequis (pageBeginRender appellé dans le mauvais sens.
		// Stack<SubForm> stack = new Stack<SubForm>();
		for (IComponent comp = getContainer(); comp != null; comp = comp
				.getContainer()) {
			if (comp instanceof SubForm) {
				throw new ApplicationRuntimeException(
						"Délibération 2007/438: il est interdit d'imbriquer des SubForms.",
						getLocation(), null);
				// stack.push((SubForm) comp);
			}
		}

		// while (!stack.isEmpty()) {
		// SubForm parent = stack.pop();
		// parent.initValue();
		// }

		// Initialisation de la valeur cible
		initValue();
		setValue(getObjectValue());
	}

	private void initValue() {
		if (getObjectValue() == null) {
			DataObject object = (DataObject) dataContext().newObject(
					getTargetEntity().getJavaClass());
			setObjectValue(object);
			setValue(object);
			if (getOninit() != null) {
				getOninit().actionTriggered(this, getPage().getRequestCycle());
			}
		} else if (getObjectValue().getObjectContext() == null) {
			// Arrive après une serialisation de la session
			getObjectValue().setObjectContext(dataContext());
		}
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		// Pop du contexte parent.
		DataContext.bindThreadDataContext(_parentContext);
	}

	/*
	 * Gestion de l'objet cible
	 */

	private ObjRelationship getRel() {
		Defense.notNull(getSource(), "source");
		ObjEntity entity = getSource().getObjectContext().getEntityResolver()
				.lookupObjEntity(getSource());
		if (entity == null) {
			throw new ApplicationRuntimeException("Pas d'entité pour "
					+ getSource(), getLocation(), null);
		}
		return (ObjRelationship) entity.getRelationship(getListe());
	}

	private ObjEntity getTargetEntity() {
		if (getValueType() != null) {
			ObjEntity entity = getObjectContext().getEntityResolver()
					.getObjEntity(getValueType());
			if (entity == null) {
				throw new ApplicationRuntimeException("Pas d'ObjEntity pour "
						+ getValueType(), getLocation(), null);
			}
			return entity;
		}
		ObjRelationship rel = getRel();
		if (rel == null) {
			throw new ApplicationRuntimeException("Pas de relation "
					+ getListe() + " pour " + getSource(), getLocation(), null);
		}
		return (ObjEntity) rel.getTargetEntity();
	}

	/*
	 * Gestion du contexte de travail
	 */

	@Persist("workflow")
	public abstract DataContext getWorkDataContext();

	public abstract void setWorkDataContext(DataContext context);

	private DataContext dataContext() {
		return getObjectContext();
	}

	@Override
	public DataContext getObjectContext() {
		if (getWorkDataContext() == null) {
			setWorkDataContext(super.getObjectContext().createChildDataContext());
		}
		return getWorkDataContext();
	}

}
