package nc.ccas.gasel.checks;

import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.Check;

public class AndCheck<T> implements Check<T> {

	private final List<Check<? super T>> checks = new LinkedList<Check<? super T>>();

	public boolean check(T value) {
		for (Check<? super T> check : checks) {
			if (!check.check(value)) {
				return false;
			}
		}
		return true;
	}

	public void add(Check<? super T> check) {
		checks.add(check);
	}

}
