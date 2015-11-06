package tests.java_gaps;

import java_gaps.Yield;
import junit.framework.TestCase;

public class TestYield extends TestCase {

	public void testYield() {
		Yield<Integer> yield = new Yield<Integer>(1, 10) {
			@Override
			protected void run(Object... params) throws InterruptedException {
				int from = (Integer) params[0];
				int to = (Integer) params[1];

				for (int i = from; i <= to; i++) {
					yield(i);
				}
			}
		};

		int lastI = -1;
		for (int i : yield) {
			lastI = i;
		}

		assert lastI == 10;
	}

}
