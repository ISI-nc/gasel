package nc.ccas.gasel.utils;

import java.util.HashMap;
import java.util.Map;

public class StarjetCoder {
	private static String table[] = { "0", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
			"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
			"Y", "Z", "$", "%", "+", "-", ".", "/", "a", "b", "c", "d", "e",
			"f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
			"s", "t", "u", "v", "w", "x", "y", "z" };

	private static Map<String, Integer> tableInv = new HashMap<String, Integer>(
			table.length);

	private static int base = 41;

	static {
		// initialisation de la table inversée
		for (int i = 0; i < table.length; i++) {
			tableInv.put(table[i], new Integer(i));
		}
	}

	public static String coder(String numero) {
		return coder(Long.parseLong(numero));
	}

	public static String coder(Number value) {
		StringBuffer retour = new StringBuffer();
		if (value != null) {
			long v = value.longValue();
			long reste = (v % base);
			long div = v / base;
			retour.insert(0, table[(int) reste]);
			while (div > 0) {
				reste = (div % base);
				div = div / base;
				retour.insert(0, table[(int) reste]);
			}
		}
		return retour.toString();
	}

	public static Long decoder(String value) {
		Long retour = null;
		if (value != null) {
			long v = 0;
			int puiss = value.length() - 1;
			for (int i = 0; i < value.length(); i++) {
				int codeChar = tableInv.get(value.substring(i, i + 1));
				v += codeChar * Math.pow(base, puiss);
				puiss--;
			}
			retour = new Long(v);
		}
		return retour;
	}
}
