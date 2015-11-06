package nc.ccas.gasel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import nc.ccas.gasel.modelUtils.DateDelta;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.hivemind.util.Defense;

public class BasePageDates {

	public static final BasePageDates INSTANCE = new BasePageDates();

	private BasePageDates() {
		// skip
	}

	public int getAnneeEnCours() {
		return new GregorianCalendar().get(Calendar.YEAR);
	}

	public int getMoisEnCours() {
		return new GregorianCalendar().get(Calendar.MONTH);
	}

	public int mois(Date d) {
		return gc(d).get(Calendar.MONTH);
	}

	public Date getDebutMois() {
		return DateUtils.debutMois();
	}

	public Date getFinMois() {
		return DateUtils.finMois();
	}

	public int annee(Date d) {
		return gc(d).get(Calendar.YEAR);
	}

	public Date debutAnnee() {
		return debutAnnee(getAnneeEnCours());
	}

	public Date debutAnnee(int annee) {
		return new GregorianCalendar(annee, Calendar.JANUARY, 1).getTime();
	}

	public Date finAnnee() {
		return finAnnee(getAnneeEnCours());
	}

	public Date finAnnee(int annee) {
		return new GregorianCalendar(annee, Calendar.DECEMBER, 31).getTime();
	}

	public Date nextMonth(Date mois) {
		GregorianCalendar gc = gc(mois);
		gc.add(Calendar.MONTH, 1);
		return gc.getTime();
	}

	public Date debutMois(Date mois) {
		return DateUtils.debutMois(mois);
	}

	public Date debutMois() {
		return debutMois(new Date());
	}

	public Date finMois(Date mois) {
		return DateUtils.finMois(mois);
	}

	public Date finMois() {
		return finMois(new Date());
	}

	private GregorianCalendar gc(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc;
	}

	public boolean checkPeriode(Date periodeDebut, Date periodeFin,
			Date objectDebut, Date objectFin) {
		Defense.notNull(periodeDebut, "periodeDebut");
		Defense.notNull(periodeFin, "periodeFin");

		if (objectDebut == null)
			return false;
		if (objectDebut.after(periodeFin))
			return false;
		if (objectFin != null && objectFin.before(periodeDebut))
			return false;
		return true;
	}

	public Date today() {
		return day(new Date()).getTime();
	}

	private GregorianCalendar day(Date date) {
		GregorianCalendar gc = gc(date);
		return new GregorianCalendar(gc.get(Calendar.YEAR), gc
				.get(Calendar.MONTH), gc.get(Calendar.DATE));
	}

	public Date prev(Date date) {
		GregorianCalendar gc = gc(date);
		gc.add(Calendar.DATE, -1);
		return gc.getTime();
	}

	public Date next(Date date) {
		GregorianCalendar gc = gc(date);
		gc.add(Calendar.DATE, 1);
		return gc.getTime();
	}

	public DateDelta sub(Date d1, Date d2) {
		return DateUtils.sub(d1, d2);
	}

	public DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

	public DateFormat formatDense = new SimpleDateFormat("dd/MM/yy");

}
