package nc.ccas.gasel;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;

import com.asystan.common.cache.CacheMap;

/**
 * Constructeur de DataObjects.
 * 
 * @author Mikaël Cluseau - ISI.NC
 */
public abstract class GaselFactory<T extends DataObject> {

	@SuppressWarnings("serial")
	private static final CacheMap<Class<? extends DataObject>, GaselFactory<?>> cache = new CacheMap<Class<? extends DataObject>, GaselFactory<?>>(
			CacheMap.HASHMAP) {

		@Override
		protected GaselFactory<?> buildValue(Class<? extends DataObject> clazz) {
			try {
				String validatorClassName = clazz.getPackage().getName()
						+ ".factory." + clazz.getSimpleName() + "Validator";
				@SuppressWarnings("rawtypes")
				Class<? extends GaselFactory> validatorClass = Class.forName(
						validatorClassName).asSubclass(GaselFactory.class);
				return validatorClass.getConstructor().newInstance();
			} catch (ClassNotFoundException e) {
				// Factory par défaut
				return new GaselFactory<DataObject>(clazz) {
					@Override
					protected void initializeObject(DataObject object,
							ObjectContext context) {
					}
				};
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	};

	public static final <E extends DataObject> GaselFactory<E> factoryFor(
			Class<E> clazz) {
		return cache.getValue(clazz).asFactoryFor(clazz);
	}

	protected abstract void initializeObject(T object, ObjectContext context);

	protected final Class<? extends T> clazz;

	protected GaselFactory(Class<? extends T> clazz) {
		this.clazz = clazz;
	}

	public T createObject() {
		return createObject(DataContext.getThreadDataContext());
	}

	public T createObject(ObjectContext context) {
		T object = clazz.cast(context.newObject(clazz));
		initializeObject(object, context);
		return object;
	}

	protected <E extends DataObject> E createObject(Class<E> clazz,
			ObjectContext context) {
		return factoryFor(clazz).createObject(context);
	}

	@SuppressWarnings("unchecked")
	public <E extends DataObject> GaselFactory<E> asFactoryFor(Class<E> clazz) {
		if (!this.clazz.isAssignableFrom(clazz)) {
			throw new RuntimeException("Factory for "
					+ this.clazz.getCanonicalName() + " cannot create "
					+ clazz.getCanonicalName());
		}
		return (GaselFactory<E>) this;
	}

}
