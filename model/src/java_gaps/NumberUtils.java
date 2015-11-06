package java_gaps;

public class NumberUtils {

	/**
	 * @param n1
	 * @param n2
	 * @return <code>n1 + n2</code>
	 */
	public static Integer add(Integer n1, Integer n2) {
		return (Integer) add((Number) n1, (Number) n2);
	}

	/**
	 * @param n1
	 * @param n2
	 * @return <code>n1 + n2</code>
	 */
	public static Double add(Double n1, Double n2) {
		return (Double) add((Number) n1, (Number) n2);
	}

	/**
	 * @param n1
	 * @param n2
	 * @return <code>n1 + n2</code>
	 */
	public static Float add(Float n1, Float n2) {
		return (Float) add((Number) n1, (Number) n2);
	}

	/**
	 * @param n1
	 * @param n2
	 * @return <code>n1 + n2</code>
	 */
	public static Long add(Long n1, Long n2) {
		return (Long) add((Number) n1, (Number) n2);
	}

	/**
	 * @param n1
	 * @param n2
	 * @return <code>n1 + n2</code>
	 */
	public static Number add(Number n1, Number n2) {
		if (n1 == null && n2 == null) {
			return null;
		} else if (n1 == null) {
			n1 = numberFrom(n2);
		} else if (n2 == null) {
			n2 = numberFrom(n1);
		}
		Number retval;
		if (n1 instanceof Double || n2 instanceof Double) {
			retval = n2.doubleValue() + n1.doubleValue();
		} else if (n1 instanceof Float || n2 instanceof Float) {
			retval = n2.floatValue() + n1.floatValue();
		} else if (n1 instanceof Long || n2 instanceof Long) {
			retval = n2.longValue() + n1.longValue();
		} else if (n1 instanceof Integer || n2 instanceof Integer) {
			retval = n2.intValue() + n1.intValue();
		} else {
			throw new RuntimeException("Non supporté: " + n1.getClass());
		}
		return retval;
	}

	/**
	 * @param n1
	 * @param n2
	 * @return <code>n1 - n2</code>
	 */
	public static Number sub(Number n1, Number n2) {
		if (n1 == null && n2 == null) {
			return null;
		} else if (n1 == null) {
			n1 = numberFrom(n2);
		} else if (n2 == null) {
			n2 = numberFrom(n1);
		}
		Number retval;
		if (n1 instanceof Double || n2 instanceof Double) {
			retval = n1.doubleValue() - n2.doubleValue();
		} else if (n1 instanceof Float || n2 instanceof Float) {
			retval = n1.floatValue() - n2.floatValue();
		} else if (n1 instanceof Long || n2 instanceof Long) {
			retval = n1.longValue() - n2.longValue();
		} else if (n1 instanceof Integer || n2 instanceof Integer) {
			retval = n1.intValue() - n2.intValue();
		} else {
			throw new RuntimeException("Non supporté: " + n1.getClass());
		}
		return retval;
	}

	private static Number numberFrom(Number n1) {
		if (n1 instanceof Double) {
			return new Double(0);
		} else if (n1 instanceof Integer) {
			return new Integer(0);
		} else if (n1 instanceof Long) {
			return new Long(0);
		} else {
			throw new RuntimeException("Non supporté: " + n1.getClass());
		}
	}

}
