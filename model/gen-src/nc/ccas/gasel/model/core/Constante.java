package nc.ccas.gasel.model.core;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.access.DataContext;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.asystan.common.cache_new.CacheMap;

@Feminin
public class Constante {

	public static Double getDouble(String nom) {
		return getValue(Double.class, nom);
	}

	public static String getString(String nom) {
		return getValue(String.class, nom);
	}

	public static Integer getInteger(String nom) {
		return getValue(Integer.class, nom);
	}

	public static <T> T getValue(Class<T> clazz, String nom) {
		return clazz.cast(CACHE.getValue(new ConstRef(clazz, nom)));
	}

	public static void clearCache() {
		CACHE.clear();
	}

	private static final CacheMap<ConstRef, Object> CACHE = new CacheMap<ConstRef, Object>(
			CacheMap.HASHMAP) {
		private static final long serialVersionUID = -1582044393284141190L;

		@Override
		protected Object buildValue(ConstRef ref) {
			Class<?> clazz = ref.clazz;
			String nom = ref.nom;

			DataContext dc = DataContext.createDataContext();
			String constEntity = "Const" + clazz.getSimpleName();
			if (dc.getEntityResolver().getObjEntity(constEntity) == null) {
				throw new RuntimeException("Pas de constantes de type "
						+ clazz.getName());
			}
			DataObject result = CommonQueries.unique(dc, constEntity, "nom",
					nom);
			if (result == null) {
				result = dc.createAndRegisterNewObject(constEntity);
				result.writeProperty("nom", nom);
				result.writeProperty("libelle", nom);
				dc.commitChanges();
				// throw new RuntimeException("Pas de constante nommée " + nom
				// + " (type:" + clazz.getName() + ")");
			}
			return result.readProperty("valeur");
		}
	};

}

class ConstRef {
	final Class<?> clazz;

	final String nom;

	public ConstRef(Class<?> clazz, String nom) {
		this.clazz = clazz;
		this.nom = nom;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(clazz).append(nom).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		ConstRef o = (ConstRef) obj;
		return new EqualsBuilder().append(clazz, o.clazz).append(nom, o.nom)
				.isEquals();
	}

}