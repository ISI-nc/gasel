package java_gaps;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateIncrement implements Increment<Date> {

	private final int years;
	private final int months;
	private final int days;
	private final int hours;
	private final int minutes;
	private final int seconds;
	private final int milliseconds;

	public DateIncrement(int years) {
		this(years, 0);
	}

	public DateIncrement(int years, int months) {
		this(years, months, 0);
	}

	public DateIncrement(int years, int months, int days) {
		this(years, months, days, 0);
	}

	public DateIncrement(int years, int months, int days, int hours) {
		this(years, months, days, hours, 0);
	}

	public DateIncrement(int years, int months, int days, int hours,
			int minutes) {
		this(years, months, days, hours, minutes, 0);
	}

	public DateIncrement(int years, int months, int days, int hours,
			int minutes, int seconds) {
		this(years, months, days, hours, minutes, seconds, 0);
	}

	public DateIncrement(int years, int months, int days, int hours,
			int minutes, int seconds, int milliseconds) {
		this.years = years;
		this.months = months;
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.milliseconds = milliseconds;
	}

	public Date next(Date object) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(object);

		gc.add(Calendar.YEAR, years);
		gc.add(Calendar.MONTH, months);
		gc.add(Calendar.DATE, days);
		gc.add(Calendar.HOUR, hours);
		gc.add(Calendar.MINUTE, minutes);
		gc.add(Calendar.SECOND, seconds);
		gc.add(Calendar.MILLISECOND, milliseconds);

		return gc.getTime();
	}

}
