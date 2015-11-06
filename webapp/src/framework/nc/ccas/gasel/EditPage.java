package nc.ccas.gasel;

import static nc.ccas.gasel.modelUtils.CommonQueries.findById;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.validator.GaselValidator;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.Fault;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.map.ObjRelationship;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * Page de base pour l'édition d'un objet.
 * 
 * @author Mikaël Cluseau - ISI.NC
 * 
 * @param <T>
 *            Le type d'objet à éditer.
 */
@ApplyWriteAccess
public abstract class EditPage<T extends DataObject> extends ObjectPage<T> {

	private GaselValidator<? super T> _validator;

	private final Class<T> clazz;

	public abstract String getActionMessage();

	public abstract void setActionMessage(String message);

	@Persist("workflow")
	public abstract DataObject getParent();

	public abstract void setParent(DataObject value);

	@Persist("workflow")
	public abstract String getParentProperty();

	public abstract void setParentProperty(String value);

	@Persist("workflow")
	public abstract boolean getWasCreated();

	public abstract void setWasCreated(boolean wasCreated);

	@Persist("workflow")
	public abstract boolean getWorkInChildContext();

	public abstract void setWorkInChildContext(boolean workInChildContext);

	@InitialValue("ognl:false")
	public abstract Boolean getIgnoreWarnings();

	@Persist("flash")
	public abstract String getMessageEnregistrement();

	public abstract void setMessageEnregistrement(String message);

	@Persist("flash")
	@InitialValue("ognl:false")
	public abstract boolean getMessageEnregistrementOk();

	public abstract void setMessageEnregistrementOk(boolean value);

	// ---------------------------------------------------------------------------------------------
	// Utile à surcharger
	//

	/**
	 * Prépare un objet créé pour l'édition.
	 */
	protected void prepareNewObject(T object) {
		// skip
	}

	protected void prepareCommit() {
		// skip
	}

	protected void prepareEnregistrer() {
		// skip
	}

	public boolean getEnregistrementDesactive() {
		return !getHasWriteAccess();
	}

	// ---------------------------------------------------------------------------------------------
	// Actions générales
	//

	public void annuler() {
		workflowClose(getRequestCycle());
	}

	public void enregistrer() {
		dontRedirect();
		if (enregistrerSansFermer()) {
			success();
		} else {
			redirect();
		}
	}

	/**
	 * Méthode pour enregistrer l'objet en cours d'édition destinée aux listener
	 * (car boolean enregistrerSansFermer() n'est pas un vrai listener).
	 */
	public void sauver() {
		enregistrerSansFermer();
	}

	private boolean checkEnregistrementDesactive = true;

	public void dontCheckEnregistrementDesactive() {
		checkEnregistrementDesactive = false;
	}

	public boolean enregistrerSansFermer() {
		if (!getHasWriteAccess()) {
			setMessageEnregistrement("Enregistrement annulé : vous n'avez pas le droit.");
			setMessageEnregistrementOk(false);
			return false;
		}
		if (checkEnregistrementDesactive) {
			if (getEnregistrementDesactive()) {
				setMessageEnregistrement("Enregistrement annulé : désactivé.");
				setMessageEnregistrementOk(false);
				return false;
			}
		} else {
			checkEnregistrementDesactive = true;
		}

		IValidationDelegate delegate = getForm().getDelegate();
		if (delegate.getHasErrors()) {
			setMessageEnregistrement("Enregistrement annulé : saisie incorrecte.");
			setMessageEnregistrementOk(false);
			return false;
		}

		// Gestion des éléments génériques
		DataObject object = getObject();
		if (object instanceof ModifListener) {
			triggerModified((ModifListener) object);
		}

		prepareEnregistrer();

		/*
		 * Validation
		 */
		GaselValidator<? super T> validator;

		// Doit-on simuler la présence d'un parent ?
		boolean simulateParent = getParent() != null
				&& getParentOfObject() == null;

		if (simulateParent) {
			DataObject fakeParent = getObjectContext().createAndRegisterNewObject(
					getParent().getClass());
			setParentOfObject(fakeParent, false);
		}
		try {
			// Validation normale
			validator = getValidator();
			validator.clear();
			validator.validate(getObject());
		} finally {
			if (simulateParent) {
				DataObject fakeParent = getParentOfObject();
				setParentOfObject(null, false);
				getObjectContext().deleteObject(fakeParent);
			}
		}
		if (!validator.getErrors().isEmpty()) {
			setMessageEnregistrement("Enregistrement annulé : il y a des erreurs.");
			setMessageEnregistrementOk(false);
			return false;
		}
		if (!getIgnoreWarnings() && !validator.getWarnings().isEmpty()) {
			setMessageEnregistrement("Enregistrement annulé : il y a des avertissements.");
			setMessageEnregistrementOk(false);
			return false;
		}

		/*
		 * Enregistrement
		 */
		prepareCommit();
		// Non autorisé CCAS/Mairie
		// getObjectContext().commitChangesToParent();
		log(">>> COMMIT");
		getObjectContext().commitChanges();
		log("<<< COMMIT");

		// -------------
		// Post-checks
		if (getObject().getPersistenceState() != PersistenceState.COMMITTED) {
			setMessageEnregistrement("Problème d'enregistrement (apparaît toujours modifié). Réessayez...");
			setMessageEnregistrementOk(false);
			return false;
		}

		if (findById(clazz, sql.idOf(getObject()),
				CayenneUtils.createDataContext()) == null) {
			setMessageEnregistrement("Problème d'enregistrement (faussement enregistré !). Réessayez...");
			setMessageEnregistrementOk(false);
			return false;
		}

		// -------------

		// Mise à jour du parent (XXX: bien ici?)
		if (getParent() != null) {
			String reverseProperty = ((ObjRelationship) getObjectContext()
					.getEntityResolver().lookupObjEntity(getObject())
					.getRelationship(getParentProperty()))
					.getReverseRelationshipName();
			getParent().writePropertyDirectly(reverseProperty, Fault.getToManyFault());
		}

		setMessageEnregistrement(getTitre() + " bien "
				+ French.feminin("enregistré", clazz) + ".");
		setMessageEnregistrementOk(true);
		redirect();
		return true;
	}

	private DataObject getParentOfObject() {
		return (DataObject) getObject().readProperty(getParentProperty());
	}

	private void setParentOfObject(DataObject parent, boolean setReverse) {
		getObject().setToOneTarget(getParentProperty(),
				ensureDataContext(parent), setReverse);
	}

	// ------------------------------------------

	// Actions sur l'objet
	public void removeFrom(String relName, DataObject value) {
		getObject().removeToManyTarget(relName, value, true);
		redirect(getRequestCycle());
	}

	public void open(int pk) {
		T object = clazz.cast(DataObjectUtils.objectForPK(getObjectContext(),
				clazz, pk));
		if (object == null) {
			throw new RuntimeException("Pas "
					+ French.particuleDe(clazz.getName()) + clazz.getName()
					+ " n°" + pk);
		}
		open(object);
	}

	public void activate() {
		activate(null);
	}

	@Override
	public void activate(ObjectCallback<?, ? super T> callback) {
		open(null, callback);
	}

	@Override
	public void open(T object) {
		open(object, null);
	}

	@Override
	public void open(T object, ObjectCallback<?, ? super T> callback) {
		openWithParent(object, null, null, callback);
	}

	/**
	 * 
	 * @param parent
	 *            Parent à définir dans l'objet édité.
	 * @param propertyName
	 *            Le nom de la relation de l'objet édité qui va recevoir le
	 *            parent. Doit être de type ToOne.
	 * @param proprietesADefinir
	 *            Valeurs à définir dans l'objet nouvellement créé. Les nulls
	 *            sont ignorés.
	 * 
	 *            Exemple :
	 *            <code>page.activateForParent(dossier, "dossier", "personne", personne)</code>
	 *            définira la personne en plus du parent.
	 */
	public void activateForParent(DataObject parent, String propertyName,
			Object... proprietesADefinir) {
		activateForParent(parent, propertyName, null, proprietesADefinir);
	}

	/**
	 * 
	 * @param parent
	 *            Parent à définir dans l'objet édité.
	 * @param propertyName
	 *            Le nom de la relation de l'objet édité qui va recevoir le
	 *            parent. Doit être de type ToOne.
	 * @param callback
	 *            L'opération à effectuer après l'enregistrement (peut être
	 *            null).
	 * @param proprietesADefinir
	 *            Valeurs à définir dans l'objet nouvellement créé. Les nulls
	 *            sont ignorés.
	 * 
	 *            Exemple :
	 *            <code>page.activateForParent(dossier, "dossier", callback, "personne", personne)</code>
	 *            définira la personne en plus du parent.
	 */
	public void activateForParent(DataObject parent, String propertyName,
			ObjectCallback<?, ? super T> callback, Object... proprietesADefinir) {
		if (proprietesADefinir.length > 0)
			dontRedirect = true;
		openWithParent(null, parent, propertyName, callback);
		if (proprietesADefinir.length > 0) {
			T object = getObject();
			for (int i = 0; i < proprietesADefinir.length; i += 2) {
				String key = (String) proprietesADefinir[i];
				Object value = proprietesADefinir[i + 1];
				if (value == null)
					continue;
				if (value instanceof DataObject) {
					value = localObject((DataObject) value);
				}
				Class<?> type = PropertyUtils.getPropertyType(object, key);
				if (!type.isInstance(value)) {
					throw new IllegalArgumentException("La propriété " + key
							+ " de la classe " + object.getClass().getName()
							+ " n'est pas de type "
							+ value.getClass().getName());
				}
				PropertyUtils.write(object, key, value);
			}
			redirect();
		}
	}

	public void openWithParent(T object, String parentProperty) {
		openWithParent(object, parentProperty, null);
	}

	public void openWithParent(T object, String parentProperty,
			ObjectCallback<?, ? super T> callback) {
		DataObject parent = (DataObject) object.readProperty(parentProperty);
		openWithParent(object, parent, parentProperty, callback);
	}

	protected void openWithParent(T object, DataObject parent,
			String parentProperty, ObjectCallback<?, ? super T> callback) {
		prepareOpen(object, callback);

		// Vérifications sur l'objet
		if (getObject() != null
				&& getObject().getObjectId().isTemporary()
				&& getObject().readPropertyDirectly(parentProperty) instanceof Fault) {
			throw new PageRedirectException("NoParent");
		}

		setParent(parent);
		setParentProperty(parentProperty);

		// Dans quel contexte travailler ?
		setWorkInChildContext(parent != null && isNew(parent));

		if (getWorkInChildContext()) {
			// setDataContext(parent.getObjectContext().createChildDataContext());
			// Non autorisé CCAS/Mairie
			throw new PageRedirectException("NoParent");
		}

		// Création si nécessaire
		if (getObject() == null) {
			setObject(createDataObject(clazz));
			prepareNewObject(getObject());
			setWasCreated(true);
		} else {
			setWasCreated(false);
		}

		// Import de la valeur dans le contexte actuel
		// Ne peut plus arriver
		// if (getObject().getObjectContext() != getObjectContext()) {
		// setObject(Utils.ensureDataContext(object, getObjectContext()));
		// }

		affecteParent();

		// fin
		redirect();
	}

	private void affecteParent() {
		T object = getObject();
		DataObject parent = getParent();
		String parentProperty = getParentProperty();
		if (parent != null && object.readProperty(parentProperty) == null) {
			DataObject localParent = ensureDataContext(parent);
			object.setToOneTarget(parentProperty, localParent, true);
		}
	}

	// ---------------------------------------------------------------------------------------------
	// Utilitaires

	@Deprecated
	protected <E extends DataObject> E createObject(Class<E> clazz) {
		return createDataObject(clazz);
	}

	// ---------------------------------------------------------------------------------------------

	protected EditPage(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		if (getObject() == null) {
			dontOpenNewEntry = true;
			dontRedirect();
			openWithParent(null, null, null, null);
		}
		affecteParent(); // Parfois il saute
		// On nettoie le validateur (erreurs doublées en cas de rewind)
		getValidator().clear();
	}

	public final GaselValidator<? super T> getValidator() {
		if (_validator == null) {
			_validator = buildValidator();
		}
		return _validator;
	}

	protected GaselValidator<? super T> buildValidator() {
		return GaselValidator.validatorFor(clazz);
	}

	// ---------------------------------------------------------------------------------------------
	// Contrôle d'accès
	//

	private Boolean _hasWriteAccess;

	public boolean getHasWriteAccess() {
		if (_hasWriteAccess == null) {
			_hasWriteAccess = getLogin().hasWriteAccess(getPageName());
		}
		return _hasWriteAccess;
	}

	// ---------------------------------------------------------------------------------------------
	// Nettoyage
	//

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_validator = null;
		_hasWriteAccess = null;
	}

}
