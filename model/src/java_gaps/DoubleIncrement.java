package java_gaps;

public class DoubleIncrement implements Increment<Double> {

	private final double increment;

	public DoubleIncrement(double increment) {
		this.increment = increment;
	}

	public Double next(Double object) {
		return object + increment;
	}

}
