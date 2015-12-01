package nc.ccas.gasel.starjet.aides;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nc.ccas.gasel.Formats;

public abstract class CourrierUtils {

	public final static DateFormat FORMAT_DATE = new SimpleDateFormat(
			"dd MMMMM yyyy", Locale.FRANCE);

	public static String dateCourrier() {
		return FORMAT_DATE.format(new Date());
	}

	public static String montant(Number montant) {
		if (montant == null)
			return "";
		return Formats.MONTANT.format(montant);
	}

	public static String designationLongue(String designation) {
		String retval;
		if (designation.equals("M") || designation.equals("Mr")) {
			retval = "Monsieur";
		} else if (designation.equals("Mme")) {
			retval = "Madame";
		} else if (designation.equals("Mlle")) {
			retval = "Mademoiselle";
		} else {
			throw new IllegalArgumentException(designation);
		}
		return String.format("%-12s", retval);
	}

}
