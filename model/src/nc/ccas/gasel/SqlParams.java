package nc.ccas.gasel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import nc.ccas.gasel.modelUtils.SqlUtils;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.Persistent;

public class SqlParams extends HashMap<String, String> {
	private static final long serialVersionUID = -1121998350594221215L;

	private static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static final String valueOf(Object value) {
		if (value instanceof String) {
			return (String) value;

		} else if (value instanceof Date) {
			return SqlUtils.dateToSql((Date) value);

		} else if (value instanceof Persistent) {
			return valueOf(DataObjectUtils.intPKForObject((Persistent) value));

		} else if (value instanceof Iterable) {
			StringBuffer buf = new StringBuffer();
			boolean first = true;
			buf.append('(');
			for (Object o : (Iterable<?>) value) {
				if (first) {
					first = false;
				} else {
					buf.append(',');
				}
				buf.append(valueOf(o));
			}
			if (first) {
				// Pas eu d'itération => collection vide
				buf.append("-1");
			}
			buf.append(')');
			return buf.toString();

		} else {
			return String.valueOf(value);
		}
	}

	public SqlParams set(String param, Object value) {
		return setRaw(param, valueOf(value));
	}

	public SqlParams setList(String param, Object... list) {
		return set(param, Arrays.asList(list));
	}

	public SqlParams setRaw(String param, String value) {
		put(param, value);
		return this;
	}

	public SqlParams setPeriode(PeriodeProps props) {
		return setPeriode(props, "debut", "fin");
	}

	public SqlParams setPeriode(PeriodeProps props, String debut, String fin) {
		return set(debut, props.getPeriodeDebut()) //
				.set(fin, props.getPeriodeFin());
	}

	public SqlParams yearMonth(String param, Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		set(param, cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH) + 1);
		return this;
	}

	public SqlParams timestamp(String param, Date timestamp) {
		return setRaw(param, TIMESTAMP_FORMAT.format(timestamp));
	}

	public SqlParams explicitTimestamp(String param, Date timestamp) {
		return setRaw(param, //
				"timestamp('" + TIMESTAMP_FORMAT.format(timestamp) + "')");
	}

}

class RawWrap {

	private final Object object;

	public RawWrap(Object object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return object == null ? "NULL" : object.toString();
	}

}
