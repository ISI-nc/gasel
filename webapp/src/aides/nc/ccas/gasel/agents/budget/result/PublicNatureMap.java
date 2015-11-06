package nc.ccas.gasel.agents.budget.result;

import static nc.ccas.gasel.modelUtils.CommonQueries.hollow;
import nc.ccas.gasel.map.AbstractCoupleMap;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.core.enums.TypePublic;

public class PublicNatureMap<V> extends
		AbstractCoupleMap<TypePublic, NatureAide, PublicNatureMap.Key, V> {
	private static final long serialVersionUID = 2417533710442245445L;

	public static class Key extends
			AbstractCoupleMap.Key<TypePublic, NatureAide> {

		public Key(TypePublic pub, NatureAide nature) {
			super(pub, nature);
		}

		@Override
		public String toString() {
			return "#<" + getClass().getSimpleName() + " nature="
					+ getNature() + " public=" + getPublic() + ">";
		}

		public NatureAide getNature() {
			return getSecond();
		}

		public TypePublic getPublic() {
			return getFirst();
		}

	};

	public PublicNatureMap() {
		super();
	}

	public PublicNatureMap(V defaultValue) {
		super(defaultValue);
	}

	public V get(TypePublic publik, int nature) {
		return get(publik, hollow(NatureAide.class, nature));
	}

	@Override
	protected Key key(TypePublic publik, NatureAide nature) {
		return new Key(publik, nature);
	}

}
