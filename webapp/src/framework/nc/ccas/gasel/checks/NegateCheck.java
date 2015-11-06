package nc.ccas.gasel.checks;

import nc.ccas.gasel.Check;

public class NegateCheck<T> implements Check<T> {

	private final Check<T> check;

	public NegateCheck(Check<T> check) {
		this.check = check;
	}

	public boolean check(T value) {
		return !check.check(value);
	}

}
