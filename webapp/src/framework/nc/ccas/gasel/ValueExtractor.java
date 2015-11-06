package nc.ccas.gasel;

public interface ValueExtractor<T, P> {

	public T extractFrom(P source);

}
