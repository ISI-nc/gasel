package java_gaps;

public class CompUtils {

	public static <T extends Comparable<? super T>> T min(T v1, T v2) {
		if (v1 == v2) {
			return v1;
		} else if (v1 == null) {
			return v2;
		} else if (v2 == null) {
			return v1;
		} else if (v1.compareTo(v2) <= 0) {
			return v1;
		} else {
			return v2;
		}
	}

	public static <T extends Comparable<? super T>> T max(T v1, T v2) {
		if (v1 == v2) {
			return v1;
		} else if (v1 == null) {
			return v2;
		} else if (v2 == null) {
			return v1;
		} else if (v1.compareTo(v2) >= 0) {
			return v1;
		} else {
			return v2;
		}
	}

}
