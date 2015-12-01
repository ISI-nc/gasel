package nc.ccas.gasel.agents.budget.result;

import static nc.ccas.gasel.modelUtils.CommonQueries.hollow;
import nc.ccas.gasel.map.AbstractCoupleMap;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.model.core.enums.TypePublic;

public class PublicStatutMap<V> extends
		AbstractCoupleMap<TypePublic, StatutAide, PublicStatutMap.Key, V> {
	private static final long serialVersionUID = 2417533710442245445L;

	public static class Key extends
			AbstractCoupleMap.Key<TypePublic, StatutAide> {

		public Key(TypePublic pub, StatutAide freq) {
			super(pub, freq);
		}

		@Override
		public String toString() {
			return "#<" + getClass().getSimpleName() + " statut="
					+ getStatut() + " public=" + getPublic() + ">";
		}

		public StatutAide getStatut() {
			return getSecond();
		}

		public TypePublic getPublic() {
			return getFirst();
		}

	};

	public PublicStatutMap() {
		super();
	}

	public PublicStatutMap(V defaultValue) {
		super(defaultValue);
	}

	public V get(TypePublic publik, int statut) {
		return get(publik, hollow(StatutAide.class, statut));
	}

	@Override
	protected Key key(TypePublic publik, StatutAide statut) {
		return new Key(publik, statut);
	}

}
