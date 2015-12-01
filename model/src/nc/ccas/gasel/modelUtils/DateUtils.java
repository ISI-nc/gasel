package nc.ccas.gasel.modelUtils;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import java_gaps.Interval;

public class DateUtils {

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy");

	public static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat(
			"yyyyMMdd_HHmmss");

	public static int dateField(Date date, int field) {
		GregorianCalendar cal = new GregorianCalendar();
		if (date != null) {
			cal.setTime(date);
		}
		return cal.get(field);
	}

	public static Date[] mois(int annee, int mois) {
		GregorianCalendar gc = new GregorianCalendar(annee, mois, 1);
		Date debut = gc.getTime();
		justBeforeNext(gc, MONTH);
		Date fin = gc.getTime();
		return new Date[] { debut, fin };
	}

	public static Date debutMois(Date mois) {
		GregorianCalendar ref = gc(mois);
		return new GregorianCalendar(ref.get(YEAR), ref.get(MONTH), 1)
				.getTime();
	}

	public static Date finMois(Date mois) {
		return justBeforeNext(debutMois(mois), MONTH);
	}

	public static Date finMois() {
		return justBeforeNext(debutMois(), Calendar.MONTH);
	}

	public static Date finMois(int annee, int mois) {
		return finMois(debutMois(annee, mois));
	}

	public static Date debutMois() {
		return debutMois(new Date());
	}

	public static Date debutMois(int annee, int mois) {
		return new GregorianCalendar(annee, mois, 1).getTime();
	}

	public static Date[] mois(Date d) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		return mois(gc.get(Calendar.YEAR), gc.get(MONTH));
	}

	public static Date[] annee(int annee) {
		GregorianCalendar gc = new GregorianCalendar(annee, Calendar.JANUARY, 1);
		Date debut = gc.getTime();
		justBeforeNext(gc, Calendar.YEAR);
		Date fin = gc.getTime();
		return new Date[] { debut, fin };
	}

	public static Date debutAnnee() {
		GregorianCalendar gc = gc(today());
		gc.set(MONTH, JANUARY);
		gc.set(DATE, 1);
		return gc.getTime();
	}

	public static Date finAnnee() {
		return justBeforeNext(debutAnnee(), Calendar.YEAR);
	}

	public static boolean apresFinMois(Date date) {
		return date.after(finMois());
	}

	public static MoisLibelle[] moisLongs() {
		return mois("MMMMM");
	}

	public static MoisLibelle[] moisCourts() {
		return mois("MMM");
	}

	public static MoisLibelle[] mois(String pattern) {
		DateFormat format = new SimpleDateFormat(pattern, Locale.FRANCE);
		MoisLibelle[] retval = new MoisLibelle[12];
		GregorianCalendar cal = new GregorianCalendar(2000, JANUARY, 1);
		for (int i = JANUARY; i <= DECEMBER; i++) {
			cal.set(MONTH, i);
			retval[i - JANUARY] = new MoisLibelle(i, format.format(cal
					.getTime()));
		}
		return retval;
	}

	private static GregorianCalendar gc() {
		return new GregorianCalendar();
	}

	public static GregorianCalendar gc(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		if (date != null)
			gc.setTime(date);
		return gc;
	}

	public static Date today() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		return gc.getTime();
	}

	public static boolean moisEnCommun(Date debut, Date fin, Date debut2,
			Date fin2) {
		return intersection(debutMois(debut), finMois(fin), debutMois(debut2),
				finMois(fin2));
	}

	/**
	 * Renvoie une version "user-friendly" de d1 - d2.
	 */
	public static DateDelta sub(Date d1, Date d2) {
		return sub(gc(d1), gc(d2));
	}

	/**
	 * Renvoie une version "user-friendly" de c1 - c2.
	 */
	public static DateDelta sub(GregorianCalendar c1, GregorianCalendar c2) {
		int ym1 = c1.get(YEAR) * 12 + c1.get(MONTH);
		int ym2 = c2.get(YEAR) * 12 + c2.get(MONTH);
		int deltaMonths = ym1 - ym2;
		DateDelta retval = new DateDelta();
		int deltaDays = c1.get(DAY_OF_MONTH) - c2.get(DAY_OF_MONTH);
		if (deltaDays < 0) {
			deltaMonths -= 1;
			deltaDays = c2.getActualMaximum(DAY_OF_MONTH)
					- c2.get(DAY_OF_MONTH) + c1.get(DAY_OF_MONTH);
		}
		retval.annees = deltaMonths / 12;
		retval.mois = deltaMonths % 12;
		retval.jours = deltaDays;
		return retval;
	}

	public static <T extends Comparable<? super T>> boolean intersection(
			T debut1, T fin1, T debut2, T fin2) {
		return (debut1 == null || fin2 == null || debut1.compareTo(fin2) <= 0)
				&& (fin1 == null || debut2 == null || fin1.compareTo(debut2) >= 0);
	}

	/**
	 * Calcul l'intersection entre [debut1, fin1] et [debut2, fin2].
	 */
	public static <T extends Comparable<? super T>> Interval<T> inter(T debut1,
			T fin1, T debut2, T fin2) {
		return new Interval<T>(debut1, fin1) //
				.intersection(new Interval<T>(debut2, fin2));
	}

	public static Date nMoisApres(int nbMois) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.MONTH, nbMois);
		return finMois(gc.getTime());
	}

	public static Date nMoisAvant(int nbMois) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.MONTH, -nbMois);
		return debutMois(gc.getTime());
	}

	public static Date transform(Date ref, int field, int delta) {
		GregorianCalendar gc = gc(ref);
		gc.add(field, delta);
		return gc.getTime();
	}

	public static Date addMonths(Date ref, int delta) {
		return transform(ref, Calendar.MONTH, delta);
	}

	public static Date addYears(Date ref, int delta) {
		return transform(ref, Calendar.YEAR, delta);
	}

	public static Date addDays(Date ref, int delta) {
		return transform(ref, Calendar.DATE, delta);
	}

	// ----

	private static void justBeforeNext(GregorianCalendar cal, int field) {
		cal.add(field, 1);
		cal.add(MILLISECOND, -1);
	}

	private static Date justBeforeNext(Date ref, int field) {
		GregorianCalendar cal = gc(ref);
		justBeforeNext(cal, field);
		return cal.getTime();
	}

	public static int anneeEnCours() {
		return gc().get(Calendar.YEAR);
	}

	public static int get(Date date, int field) {
		return gc(date).get(field);
	}

	public static int anneeMois(Date date) {
		GregorianCalendar cal = gc(date);
		return anneeMois(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
	}

	public static int anneeMois(int annee, int mois) {
		return annee * 12 + mois + 1;
	}

	public static String timestamp() {
		return TIMESTAMP_FORMAT.format(new Date());
	}

	public static Timestamp sqlTimestamp(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return new Timestamp(cal.getTimeInMillis());
	}

}
