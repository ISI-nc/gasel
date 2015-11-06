package test;

import junit.framework.TestCase;
import nc.ccas.gasel.CompositeHashMap;

public class TestCompositeHashMap extends TestCase {

	public void testPutGetVarArg() throws Exception {
		CompositeHashMap map = new CompositeHashMap();
		Object x = new Object();

		map.put("a", 1, x);
		
		assertTrue(map.contains("a", 1));
		assertEquals(x, map.get("a", 1));
	}

	public void testPutGetArray() throws Exception {
		Object[] key = new Object[] { "a", 1 };
		
		CompositeHashMap map = new CompositeHashMap();
		Object x = new Object();

		map.put(key, x);
		
		assertTrue(map.contains(key));
		assertEquals(x, map.get(key));
		
		assertTrue(map.contains(new Object[] { "a", 1 }));
		assertEquals(x, map.get(new Object[] { "a", 1 }));
	}

	public void testPutGetMixedVarArgArray() throws Exception {
		Object[] key = new Object[] { "a", 1 };
		
		CompositeHashMap map = new CompositeHashMap();
		Object x = new Object();

		map.put(key, x);
		
		assertTrue(map.contains("a", 1));
		assertEquals(x, map.get("a", 1));
	}

	public void testPutGetMixedArrayVarArg() throws Exception {
		Object[] key = new Object[] { "a", 1 };
		
		CompositeHashMap map = new CompositeHashMap();
		Object x = new Object();

		map.put("a", 1, x);

		assertTrue(map.contains(key));
		assertEquals(x, map.get(key));
	}

}
