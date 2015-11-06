package java_gaps;

public class MinMax<T extends Comparable<? super T>> {

	private T min = null;
	private T max = null;

	public void inject(T value) {
		if (value == null)
			return;

		if (min == null) {
			min = value;
			max = value;
			return;
		}

		if (min.compareTo(value) > 0)
			min = value;

		if (max.compareTo(value) < 0)
			max = value;
	}

	public void inject(T... values) {
		for (T value : values)
			inject(value);
	}

	public void inject(Iterable<T> values) {
		for (T value : values)
			inject(value);
	}

	public T min() {
		return min;
	}

	public T max() {
		return max;
	}

	public T getMin() {
		return min;
	}

	public T getMax() {
		return max;
	}

}
