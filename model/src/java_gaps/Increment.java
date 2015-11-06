package java_gaps;

public interface Increment<T> {

	/**
	 * The next element after <code>object</code>. Never returns
	 * <code>null</code>.
	 * 
	 * @param object
	 *            The reference. May not be null.
	 */
	public T next(T object);

}
