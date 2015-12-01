package nc.ccas.gasel.validator;

import static com.asystan.common.cayenne.QueryFactory.createAnd;
import static com.asystan.common.cayenne.QueryFactory.createEquals;
import static com.asystan.common.cayenne.QueryFactory.createNot;
import static com.asystan.common.cayenne.QueryFactory.createOr;
import static org.apache.cayenne.DataObjectUtils.intPKForObject;
import static nc.ccas.gasel.modelUtils.CayenneUtils.createDataContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.ccas.gasel.CheckResult;
import nc.ccas.gasel.French;
import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.map.ObjAttribute;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.validation.BeanValidationFailure;
import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;

/**
 * Classe de base pour la validation et le système d'alertes.
 * 
 * @author Mikaël Cluseau - ISI.NC
 * 
 * @param <T>
 *            Le type d'objet à valider.
 */
public abstract class GaselValidator<T extends DataObject> implements
		CheckResult {

	@SuppressWarnings("unchecked")
	public static final <E extends DataObject> GaselValidator<E> validatorFor(
			Class<E> clazz) {
		try {
			String validatorClassName = clazz.getPackage().getName()
					+ ".valid." + clazz.getSimpleName() + "Validator";
			@SuppressWarnings("rawtypes")
			Class<? extends GaselValidator> validatorClass = Class.forName(
					validatorClassName).asSubclass(GaselValidator.class);
			return validatorClass.getConstructor().newInstance()
					.asValidatorFor(clazz);
		} catch (ClassNotFoundException e) {
			// Pas de validateur spécifique => on utilise un validateur par
			// défaut (consistance DB seulement).
			return new GaselValidator<E>(clazz) {
				@Override
				public void validateImpl(E object) {
					validateDataObject(object);
				}
			};
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract void validateImpl(T object);

	private final List<String> errors = new ArrayList<String>();

	private final List<String> warnings = new ArrayList<String>();

	private final Class<T> clazz;

	private final Map<String, Class<?>> fakes = new HashMap<String, Class<?>>();

	protected final GVDates dates = new GVDates();

	/**
	 * @param clazz
	 *            Nécessaire car la classe utilisée pour <code>T</code> n'est
	 *            pas connue à l'exécution.
	 */
	protected GaselValidator(Class<T> clazz) {
		this.clazz = clazz;
	}

	public final void validate(T object) {
		ObjectContext workDc = object.getObjectContext();// .createChildDataContext();
		T obj = (T) workDc.localObject(object.getObjectId(), object);

		Set<DataObject> toDelete = new HashSet<DataObject>();
		try {
			if (!fakes.isEmpty()) {
				// Simulation des fakes
				for (String prop : fakes.keySet()) {
					if (obj.readProperty(prop) != null)
						continue;
					DataObject value = (DataObject) workDc.newObject(fakes
							.get(prop));
					obj.setToOneTarget(prop, value, false);
					toDelete.add(value);
				}
			}
			// Validation
			validateImpl(obj);
		} finally {
			// Annulation des modifs
			if (!fakes.isEmpty()) {
				// Suppression des fakes
				for (String prop : fakes.keySet()) {
					DataObject value = (DataObject) obj.readProperty(prop);
					if (!toDelete.contains(value))
						continue;
					workDc.deleteObject(value);
				}
			}
		}
		// childDc.rollbackChangesLocally();
	}

	@SuppressWarnings("unchecked")
	public final <E extends DataObject> GaselValidator<E> asValidatorFor(
			Class<E> clazz) {
		if (!this.clazz.isAssignableFrom(clazz)) {
			throw new RuntimeException("Validator for "
					+ this.clazz.getCanonicalName() + " cannot validate "
					+ clazz.getCanonicalName());
		}
		return (GaselValidator<E>) this;
	}

	/**
	 * Ajoute un <code>message</code> dans les erreurs. Les erreurs sont
	 * bloquantes et empêchent la sauvegarde de l'objet cible.
	 */
	protected void error(String message) {
		errors.add(message);
	}

	/**
	 * Comme <code>error(String message)</code>, mais utilise
	 * <code>warning(String message)</code> si <code>isError</code> est
	 * <code>false</code>.
	 */
	protected void error(boolean isError, String message) {
		if (isError) {
			error(message);
		} else {
			warning(message);
		}
	}

	/**
	 * Comme <code>error(boolean isError, String message)</code>, mais seulement
	 * si <code>onError</code> est à <code>true</code>.
	 */
	protected void error(boolean isError, String message, boolean onError) {
		if (!onError)
			return;
		error(isError, message);
	}

	/**
	 * Comme <code>error(String message)</code>, mais seulement si
	 * <code>onError</code> est à <code>true</code>.
	 */
	protected void error(String message, boolean onError) {
		if (!onError)
			return;
		error(message);
	}

	/**
	 * Ajoute des <code>messages</code> dans les erreurs. Les erreurs sont
	 * bloquantes et empêchent la sauvegarde de l'objet cible.
	 */
	protected void errors(List<String> messages) {
		for (String m : messages) {
			error(m);
		}
	}

	/**
	 * Ajoute un <code>message</code> dans les avertissements. Les
	 * avertissements ne sont pas bloquants mais demandent à l'utilisateur de
	 * confirmer sa saisie.
	 */
	protected void warning(String message) {
		warnings.add(message);
	}

	/**
	 * Comme <code>warning(String message)</code>, mais seulement si
	 * <code>onError</code> est à <code>true</code>.
	 */
	protected void warning(String message, boolean onError) {
		if (!onError)
			return;
		warning(message);
	}

	/**
	 * Ajoute des <code>messages</code> dans les avertissements. Les
	 * avertissements ne sont pas bloquants mais demandent à l'utilisateur de
	 * confirmer sa saisie.
	 */
	protected void warnings(List<String> messages) {
		for (String m : messages) {
			warning(m);
		}
	}

	public void clear() {
		errors.clear();
		warnings.clear();
	}

	public List<String> getErrors() {
		return Collections.unmodifiableList(errors);
	}

	public List<String> getWarnings() {
		return Collections.unmodifiableList(warnings);
	}

	/**
	 * Valide l'objet <code>o</code> avec son validateur, en mettant
	 * <code>prefix</code> devant chaque erreur et chaque warning.
	 */
	@SuppressWarnings("unchecked")
	// it IS checked!!
	protected <E extends DataObject> boolean subValidate(String prefix, E o) {
		GaselValidator<E> subValidator = (GaselValidator<E>) validatorFor(o
				.getClass());
		subValidator.validate(o);
		boolean retval = true;
		for (String message : subValidator.errors) {
			error(prefix + " : " + suitePhrase(message));
			retval = false;
		}
		for (String message : subValidator.warnings) {
			warning(prefix + " : " + suitePhrase(message));
			// retval = false;
		}
		return retval;
	}

	// ------------------------------------------------------------------------
	// Validation des DataObjects
	//

	protected boolean validateDataObject(DataObject object) {
		boolean retval = true;
		retval &= checkDataObjectValidation(object);
		if (hasPeriode(object)) {
			if (!checkPeriode(object)) {
				error("Début avant la fin.");
				retval = false;
			}
		}
		return retval;
	}

	protected boolean checkDataObjectValidation(DataObject object) {
		ValidationResult vr = new ValidationResult();
		switch (object.getPersistenceState()) {
		case PersistenceState.NEW:
			((CayenneDataObject) object).validateForInsert(vr);
			break;
		case PersistenceState.MODIFIED:
			((CayenneDataObject) object).validateForUpdate(vr);
			break;
		case PersistenceState.DELETED:
			((CayenneDataObject) object).validateForDelete(vr);
			break;
		}
		for (Object o : vr.getFailures()) {
			ValidationFailure vf = (ValidationFailure) o;
			String message = vf.getDescription();
			if (vf instanceof BeanValidationFailure) {
				BeanValidationFailure bvf = (BeanValidationFailure) vf;
				if (bvf.getProperty().equals("modification"))
					continue; // Défini automatiquement
				// Champ requis non rempli
				for (String tail : Arrays.asList(" is required.",
						" is a required field.", " can not be empty.")) {
					if (message.endsWith(tail)) {
						message = propertyMissing(propertyName(object, bvf
								.getProperty()));
						break;
					}
				}
			}
			error(message);
		}
		return vr.getFailures().isEmpty();
	}

	// ------------------------------------------------------------------------
	// Périodes
	//

	protected boolean checkPeriode(DataObject object, String debut, String fin) {
		return checkPeriode(object, //
				(Date) object.readProperty(debut), //
				(Date) object.readProperty(fin));
	}

	private boolean hasPeriode(DataObject object) {
		ObjEntity ent = entityFor(object);
		ObjAttribute attrDebut = (ObjAttribute) ent.getAttribute("debut");
		ObjAttribute attrFin = (ObjAttribute) ent.getAttribute("fin");
		return attrDebut != null && attrDebut.getJavaClass() == Date.class
				&& attrFin != null && attrFin.getJavaClass() == Date.class;
	}

	private boolean checkPeriode(DataObject object) {
		return checkPeriode(object, "debut", "fin");
	}

	protected boolean checkPeriode(DataObject object, Date debut, Date fin) {
		return debut == null || fin == null || !debut.after(fin);
	}

	protected ObjEntity entityFor(DataObject object) {
		return object.getObjectContext().getEntityResolver().lookupObjEntity(
				object);
	}

	protected boolean checkDateBefore(Date first, Date second) {
		if (first == null || second == null)
			return false;
		return first.before(second);
	}

	// ------------------------------------------------------------------------
	// Unicité
	//

	/**
	 * @param object
	 *            L'objet référence, qui sera ignoré dans la requète.
	 * @param fieldsAndValues
	 *            Les couples champ/valeur à vérifier.
	 * 
	 * @return <code>true</code> ssi il n'y a pas de double dans la base de
	 *         données.
	 */
	protected <E extends DataObject> boolean checkUnicity(E object,
			Object... fieldsAndValues) {
		assert fieldsAndValues.length % 2 == 0;

		List<Expression> exprs = new LinkedList<Expression>();
		for (int i = 0; i < fieldsAndValues.length; i += 2) {
			String[] fields = ((String) fieldsAndValues[i]).split("\\s*,\\s*");
			Object value = fieldsAndValues[i + 1];
			Expression[] fieldExprs = new Expression[fields.length];
			for (int j = 0; j < fields.length; j++) {
				fieldExprs[j] = createEquals(fields[j], value);
			}
			exprs.add(createOr(fieldExprs));
		}

		if (!object.getObjectId().isTemporary()) {
			exprs.add(createNot(createEquals("db:id", intPKForObject(object))));
		}

		Expression expr = createAnd(exprs);
		return createDataContext().performQuery(
				new SelectQuery(object.getClass(), expr)).isEmpty();
	}

	// ------------------------------------------------------------------------
	// Présence de propriétés
	//

	/**
	 * Comme <code>require</code>, mais valide en profondeur les
	 * <code>DataObject</code>s.
	 * 
	 * @param error
	 *            true si l'absence est une erreur, false si c'est juste un
	 *            warning.
	 * @param source
	 *            L'objet source.
	 * @param property
	 *            Le nom de la propriété à vérifier.
	 * @return true ssi la propriété est définie et n'a pas d'erreurs.
	 */
	protected boolean requireProperty(boolean error, DataObject source,
			String property) {
		Object value = source.readProperty(property);
		String propertyName = propertyName(source, property);
		if (require(error, value, propertyName)) {
			if (value instanceof DataObject) {
				return subValidate(propertyName, (DataObject) value);
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Vérifie qu'une valeur est définie.
	 * 
	 * @param error
	 *            true si l'absence est une erreur, false si c'est juste un
	 *            warning.
	 * @param value
	 *            La valeur qui doit être définie.
	 * @param property
	 *            Le nom (humain) de la propriété vérifiée, utilisé pour
	 *            construire le message d'erreur.
	 * @return true ssi la propriété est définie.
	 */
	protected boolean require(boolean error, Object value, String propertyName) {
		if (value == null) {
			error(error, propertyMissing(propertyName));
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Simule la présence d'une valeur.
	 * 
	 * Seules les relations toOne sont supportées.
	 * 
	 * @param property
	 *            La propriété à simuler.
	 */
	public void fake(String property) {
		// Test de la propriété
		ObjEntity ent = objEntity();
		ObjRelationship rel = (ObjRelationship) ent.getRelationship(property);
		if (rel == null) {
			throw new IllegalArgumentException("Pas de relation "
					+ ent.getName() + "." + property);
		}
		if (rel.isToMany()) {
			throw new IllegalArgumentException("La relation " + ent.getName()
					+ "." + property + " n'est pas un toOne");
		}
		ObjEntity targetEntity = (ObjEntity) rel.getTargetEntity();
		// Ok, ajout.
		fakes.put(property, targetEntity.getJavaClass());
	}

	private ObjEntity objEntity() {
		return CayenneUtils.entityResolver().lookupObjEntity(clazz);
	}

	protected boolean isInDb(DataObject object) {
		return !object.getObjectId().isTemporary();
	}

	/*
	 * --------------------------------------------------------------------------
	 * ------------------- Messages standard
	 */

	protected static String propertyMissing(String propertyName) {
		StringBuilder message = new StringBuilder("Pas ");
		message.append(French.particuleDe(propertyName));
		message.append(propertyName);
		return message.toString();
	}

	protected static String suitePhrase(String message) {
		return message.substring(0, 1).toLowerCase() + message.substring(1);
	}

	protected static String propertyName(Object object, String property) {
		return French.propertyName(object, property);
	}

}
