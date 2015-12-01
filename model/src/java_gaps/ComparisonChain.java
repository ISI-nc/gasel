package java_gaps;

import java.util.Comparator;

public class ComparisonChain {

	private int result = 0;

	/**
	 * @return The result of the comparisons done.
	 */
	public int result() {
		return result;
	}

	/**
	 * 2 &gt; 1
	 * 
	 * @return <code>this</code>
	 */
	public ComparisonChain append(long v1, long v2) {
		if (result == 0 && v1 != v2) {
			result = (v1 < v2) ? -1 : 1;
		}
		return this;
	}

	/**
	 * 0.2 &gt; 0.1
	 * 
	 * @return <code>this</code>
	 */
	public ComparisonChain append(double v1, double v2) {
		if (result == 0 && v1 != v2) {
			result = (v1 < v2) ? -1 : 1;
		}
		return this;
	}

	/**
	 * true &gt; false
	 * 
	 * @return <code>this</code>
	 */
	public ComparisonChain append(boolean v1, boolean v2) {
		if (result == 0 && v1 != v2) {
			result = (v2) ? -1 : 1;
		}
		return this;
	}

	/**
	 * Compares v1 and v2, putting <code>null</code>s before.
	 * 
	 * @return <code>this</code>
	 */
	public <E extends Comparable<? super E>> ComparisonChain append(E v1, E v2) {
		return append(v1, v2, false);
	}

	/**
	 * Compares v1 and v2, putting <code>null</code>s before iff
	 * <code>nullAfter</code> is <code>false</code>.
	 * 
	 * @return <code>this</code>
	 */
	public <E extends Comparable<? super E>> ComparisonChain append(E v1, E v2,
			boolean nullAfter) {
		if (result == 0 && v1 != v2) {
			if (v1 == v2) {
				// ignore
			} else if (v1 == null) {
				result = nullAfter ? 1 : -1;
			} else if (v2 == null) {
				result = nullAfter ? -1 : 1;
			} else {
				result = v1.compareTo(v2);
			}
		}
		return this;
	}

	/**
	 * Compares <code>v1</code> and <code>v2</code> using the given
	 * <code>comparator</code>, putting <code>null</code>s before.
	 * 
	 * @return <code>this</code>
	 */
	public <E> ComparisonChain append(E v1, E v2,
			Comparator<? super E> comparator) {
		return append(v1, v2, comparator, false);
	}

	/**
	 * Compares <code>v1</code> and <code>v2</code> using the given
	 * <code>comparator</code>, putting <code>null</code>s before iff
	 * <code>nullAfter</code> is <code>false</code>
	 * 
	 * @return <code>this</code>
	 */
	public <E> ComparisonChain append(E v1, E v2,
			Comparator<? super E> comparator, boolean nullAfter) {
		if (result == 0) {
			if (v1 == v2) {
				// ignore
			} else if (v1 == null) {
				result = nullAfter ? 1 : -1;
			} else if (v2 == null) {
				result = nullAfter ? -1 : 1;
			} else {
				result = comparator.compare(v1, v2);
			}
		}
		return this;
	}

}
