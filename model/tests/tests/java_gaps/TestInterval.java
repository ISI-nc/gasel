package tests.java_gaps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java_gaps.IntegerIncrement;
import java_gaps.Interval;
import junit.framework.TestCase;

public class TestInterval extends TestCase {

	public void testIntersectionExistante() throws Exception {
		Interval<Integer> i1 = new Interval<Integer>(1, 3);
		Interval<Integer> i2 = new Interval<Integer>(2, 4);

		assertEquals(new Interval<Integer>(2, 3), i1.intersection(i2));
		assertEquals(new Interval<Integer>(2, 3), i2.intersection(i1));
	}

	public void testIntersectionExistanteAvecNulls() throws Exception {
		Interval<Integer> i1 = new Interval<Integer>(null, 3);
		Interval<Integer> i2 = new Interval<Integer>(2, null);

		assertEquals(new Interval<Integer>(2, 3), i1.intersection(i2));
		assertEquals(new Interval<Integer>(2, 3), i2.intersection(i1));
	}

	public void testIntersectionInexistante() throws Exception {
		Interval<Integer> i1 = new Interval<Integer>(1, 2);
		Interval<Integer> i2 = new Interval<Integer>(3, 4);

		assertNull(i1.intersection(i2));
		assertNull(i2.intersection(i1));
	}

	public void testIntersectionInexistanteAvecNulls() throws Exception {
		Interval<Integer> i1 = new Interval<Integer>(null, 2);
		Interval<Integer> i2 = new Interval<Integer>(3, null);

		assertNull(i1.intersection(i2));
		assertNull(i2.intersection(i1));
	}

	public void testEquals() throws Exception {
		assertEquals( //
				new Interval<Integer>(1, 2), //
				new Interval<Integer>(1, 2));
		assertEquals( //
				new Interval<Integer>(null, 2), //
				new Interval<Integer>(null, 2));
		assertEquals( //
				new Interval<Integer>(1, null), //
				new Interval<Integer>(1, null));
		assertEquals( //
				new Interval<Integer>(null, null), //
				new Interval<Integer>(null, null));
	}

	public void testToString() throws Exception {
		assertEquals("[1, 2]", new Interval<Integer>(1, 2).toString());
		assertEquals("[null, 2]", new Interval<Integer>(null, 2).toString());
		assertEquals("[1, null]", new Interval<Integer>(1, null).toString());
		assertEquals("[null, null]", new Interval<Integer>(null, null)
				.toString());
	}

	public void testHashCode() throws Exception {
		new Interval<Integer>(1, 2).hashCode();
		new Interval<Integer>(null, 2).hashCode();
		new Interval<Integer>(1, null).hashCode();
		new Interval<Integer>(null, null).hashCode();
	}

	public void testIteration() throws Exception {
		List<Integer> values = new ArrayList<Integer>();
		for (Integer i : new Interval<Integer>(1, 4)
				.iterate(new IntegerIncrement(1))) {
			values.add(i);
		}
		assertEquals(Arrays.asList(1, 2, 3, 4), values);
	}

}
