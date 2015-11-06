package java_gaps;

public class IntegerIncrement implements Increment<Integer> {

	private final int increment;

	public IntegerIncrement(int increment) {
		this.increment = increment;
	}

	public Integer next(Integer object) {
		return object + increment;
	}

}
