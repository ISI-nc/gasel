package nc.ccas.gasel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Formats {

	public static final String NBSP = "\u00A0";

	public static final Formats INSTANCE = new Formats();

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy");

	public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

	public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm");

	private static final DecimalFormatSymbols SYMBOLES_NUM_FRANCAIS = new DecimalFormatSymbols();
	static {
		SYMBOLES_NUM_FRANCAIS.setGroupingSeparator(' ');
		SYMBOLES_NUM_FRANCAIS.setDecimalSeparator(',');
	}

	public static final NumberFormat MONTANT = new DecimalFormat("#,##0.##",
			SYMBOLES_NUM_FRANCAIS);

	public static final NumberFormat POURCENT = new DecimalFormat("#0.00%",
			SYMBOLES_NUM_FRANCAIS);

	public static final NumberFormat NUMERO_DOSSIER = new DecimalFormat(
			"####,####", SYMBOLES_NUM_FRANCAIS);

	public static final DateFormat MOIS_LONG = new SimpleDateFormat("MMMMM", Locale.FRANCE);

	public static final DateFormat MOIS_LONG_ANNEE = new SimpleDateFormat(
			"MMMMM yyyy", Locale.FRANCE);

	public static final DateFormat MOIS = new SimpleDateFormat("MM");

	public static final DateFormat MOIS_ANNEE = new SimpleDateFormat("MM/yyyy");

	private Formats() {
		// skip
	}

	public DateFormat getDate() {
		return DATE_FORMAT;
	}

	public DateFormat getTime() {
		return TIME_FORMAT;
	}

	public DateFormat getDateTime() {
		return DATE_TIME_FORMAT;
	}

	public NumberFormat getMontant() {
		return MONTANT;
	}

	public NumberFormat getPourcent() {
		return POURCENT;
	}

	public NumberFormat getNumeroDossier() {
		return NUMERO_DOSSIER;
	}

}
