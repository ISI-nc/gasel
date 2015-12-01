package nc.ccas.gasel.checks;

import java.util.Date;

import nc.ccas.gasel.Check;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.cayenne.DataObject;

public class PeriodeActive implements Check<DataObject> {

	public static final PeriodeActive INSTANCE = new PeriodeActive();

	private PeriodeActive() {
		// skip
	}

	public boolean check(DataObject value) {
		Date now = new Date();
		Date debut = (Date) value.readProperty("debut");
		Date fin = (Date) value.readProperty("fin");
		return DateUtils.intersection(now, now, debut, fin);
	}

}
