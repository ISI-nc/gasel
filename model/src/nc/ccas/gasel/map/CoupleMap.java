package nc.ccas.gasel.map;

public class CoupleMap<K1, K2, V> extends
		AbstractCoupleMap<K1, K2, AbstractCoupleMap.Key<K1, K2>, V> {
	private static final long serialVersionUID = 6968921358088986048L;

	@Override
	protected Key<K1, K2> key(K1 first, K2 second) {
		return new Key<K1, K2>(first, second);
	}

}
