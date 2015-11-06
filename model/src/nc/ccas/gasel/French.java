package nc.ccas.gasel;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.asystan.common.cache.CacheMap;

public class French {

	public static String particuleDe(String word) {
		if (word.matches("h?[aeiouAEIOU].*")) {
			return "d'";
		} else {
			return "de ";
		}
	}

	public static String propertyName(Object object, String property) {
		return propertyName(object.getClass(), property);
	}

	public static String propertyName(Class<?> clazz, String property) {
		ResourceBundle bundle = BUNDLES.getValue(clazz);
		if (bundle == null) {
			return property;
		}
		try {
			return bundle.getString(property);
		} catch (MissingResourceException e) {
			return property;
		}
	}

	@SuppressWarnings("serial")
	private static final CacheMap<Class<?>, ResourceBundle> BUNDLES = new CacheMap<Class<?>, ResourceBundle>(
			CacheMap.HASHMAP) {
		@Override
		protected ResourceBundle buildValue(Class<?> key) {
			try {
				return ResourceBundle.getBundle(key.getName(), Locale.FRENCH);
			} catch (MissingResourceException e) {
				return null;
			}
		};
	};

	// ---------------------------
	// Fonctions sur les nombres
	//

	private static final String[] CHIFFRES = new String[] { "zéro", "un",
			"deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf",
			"dix", "onze", "douze", "treize", "quatorze", "quinze", "seize" };

	private static final Object[][] BORNES = new Object[][] {
			new Object[] { (long) 1e9, "milliard", true },
			new Object[] { (long) 1e6, "million", true },
			new Object[] { (long) 1e3, "mille", false },
			new Object[] { (long) 1e2, "cent", false } //
	};

	private static final Object[][] DIZAINES = new Object[][] {
			new Object[] { 80l, "quatre-vingt", false },
			new Object[] { 60l, "soixante", true },
			new Object[] { 50l, "cinquante", true },
			new Object[] { 40l, "quarante", true },
			new Object[] { 30l, "trente", true },
			new Object[] { 20l, "vingt", true } //
	};

	public static String nombreToutesLettres(long total) {
		if (total == 0)
			return CHIFFRES[0]; // seul cas où on a zéro

		StringBuilder buf = new StringBuilder();

		long reste = total;
		for (Object[] borne_texte : BORNES) {
			long borne = (Long) borne_texte[0];
			String texte = (String) borne_texte[1];
			boolean afficherUn = (Boolean) borne_texte[2];

			if (reste < borne)
				continue;
			if (buf.length() > 0)
				buf.append(" ");

			long auDessus = reste / borne;
			reste = reste - auDessus * borne;

			if (afficherUn) {
				buf.append(nombreToutesLettres(auDessus)).append(" ");
				buf.append(pluriel(texte, auDessus));
			} else {
				if (auDessus != 1)
					buf.append(nombreToutesLettres(auDessus)).append(" ");
				buf.append(texte);
			}
		}
		// Ici reste < 100
		for (Object[] borne_texte : DIZAINES) {
			long borne = (Long) borne_texte[0];
			String texte = (String) borne_texte[1];
			boolean liaisonAuUn = (Boolean) borne_texte[2];

			if (reste < borne)
				continue;
			if (buf.length() > 0)
				buf.append(" ");

			long auDessus = reste - reste / borne * borne;
			reste = 0;

			buf.append(texte);
			if (auDessus == 0)
				continue;
			if ((auDessus == 1 || auDessus == 11) && liaisonAuUn)
				buf.append("-et-");
			else
				buf.append(" ");
			buf.append(nombreToutesLettres(auDessus));
		}

		// Ici reste < 20
		if (reste > 16) {
			if (buf.length() > 0)
				buf.append(" ");
			buf.append(CHIFFRES[10] + "-" + CHIFFRES[(int) reste % 10]);
			reste = 0;
		} else if (reste > 0) {
			if (buf.length() > 0)
				buf.append(" ");
			buf.append(CHIFFRES[(int) reste]);
			reste = 0;
		}

		return buf.toString();
	}

	public static String chiffre(int chiffre) {
		return CHIFFRES[chiffre];
	}

	// ------------------------------------------------------------------------
	// Accords genre/nombre

	public static String pluriel(String string, long nombre) {
		if (nombre == 1)
			return string;
		return string + "s";
	}

	public static String feminin(String string, Object object) {
		return feminin(string, object.getClass());
	}

	public static String feminin(String string, Class<?> clazz) {
		if (clazz.getAnnotation(Feminin.class) == null)
			return string;
		if (string.endsWith("teur")) {
			return string.substring(0, string.length() - 4) + "trice";
		}
		// Others go here...
		return string + "e";
	}

}
