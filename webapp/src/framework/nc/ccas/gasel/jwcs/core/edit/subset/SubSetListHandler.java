package nc.ccas.gasel.jwcs.core.edit.subset;

/**
 * Normalized interface to a SubSet's list.
 * 
 * @author ISI.NC - Mikaël Cluseau
 * 
 * @param <T>
 *            Collection's element type.
 */
public interface SubSetListHandler<T> extends Iterable<T> {

	/**
	 * Returns <code>true</code> iff <code>value</code> is in the list
	 * represented by this handler.
	 */
	public boolean contains(T value);

	/**
	 * Adds <code>value</code> to the list represented by this handler.
	 */
	public void add(T value);

	/**
	 * Removes <code>value</code> from the list represented by this handler.
	 */
	public void remove(T value);

}
