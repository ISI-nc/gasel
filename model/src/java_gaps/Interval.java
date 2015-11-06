package java_gaps;

import static java_gaps.CompUtils.max;
import static java_gaps.CompUtils.min;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Interval<T extends Comparable<? super T>> {

	public final T start;
	public final T end;

	public Interval(T start, T end) {
		this.start = start;
		this.end = end;
	}

	public Interval<T> intersection(Interval<T> other) {
		Interval<T> retval = new Interval<T>( //
				max(start, other.start), //
				min(end, other.end));

		if (retval.start.compareTo(retval.end) > 0)
			return null;

		return retval;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Interval))
			return false;

		Interval<?> o = (Interval<?>) obj;

		return new EqualsBuilder() //
				.append(start, o.start) //
				.append(end, o.end) //
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder() //
				.append(start) //
				.append(end) //
				.toHashCode();
	}

	@Override
	public String toString() {
		return "[" + start + ", " + end + "]";
	}

	// ------------------------------------------------------------------
	// Iterable support
	//

	public Iterable<T> iterate(Increment<T> incrementor) {
		return new IntervalIterable(incrementor);
	}

	private class IntervalIterable implements Iterable<T> {
		private final Increment<T> incrementor;

		public IntervalIterable(Increment<T> incrementor) {
			this.incrementor = incrementor;
		}

		public Iterator<T> iterator() {
			return new IntervalIterator(incrementor);
		}
	}

	private class IntervalIterator implements Iterator<T> {
		private final Increment<T> incrementor;
		private T currentValue;

		public IntervalIterator(Increment<T> incrementor) {
			this.incrementor = incrementor;
			this.currentValue = null;
		}

		public boolean hasNext() {
			// No iteration for infinite intervals
			if (start == null || end == null)
				return false;

			return currentValue == null
					|| incrementor.next(currentValue).compareTo(end) <= 0;
		}

		public T next() {
			if (currentValue == null) {
				currentValue = start;
			} else {
				currentValue = incrementor.next(currentValue);
				if (currentValue.compareTo(end) > 0)
					throw new NoSuchElementException();
			}
			return currentValue;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
