package nc.ccas.gasel.checks;

import java.util.Date;

import nc.ccas.gasel.Check;

public abstract class DateInPeriode<T> implements Check<T> {

	protected abstract Date getDate(T value);

	private final Date debut;
	private final Date fin;

	public DateInPeriode(Date debut, Date fin) {
		this.debut = debut;
		this.fin = fin;
	}

	public boolean check(T value) {
		Date date = getDate(value);
		if (date == null)
			return false;
		return date.compareTo(debut) >= 0 && date.compareTo(fin) <= 0;
	}

}
