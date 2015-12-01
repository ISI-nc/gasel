package nc.ccas.gasel.validator;

import static nc.ccas.gasel.modelUtils.DateUtils.today;

import java.util.Date;
import java.util.List;

import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.cayenne.DataObject;

public class GVDates {

	public boolean checkPassee(Date date) {
		return checkPassee(date, false);
	}

	public boolean checkPassee(Date date, boolean strict) {
		return checkOrder(date, today(), strict);
	}

	public boolean checkFuture(Date date) {
		return checkFuture(date, false);
	}

	public boolean checkFuture(Date date, boolean strict) {
		return checkOrder(today(), date, strict);
	}

	public boolean checkOrder(Date first, Date second) {
		return checkOrder(first, second, false);
	}

	public boolean checkOrder(Date first, Date second, boolean strict) {
		if (first == null || second == null) {
			return true;
		}
		if (strict) {
			return first.compareTo(second) < 0;
		} else {
			return first.compareTo(second) <= 0;
		}
	}

	public boolean checkRecouvrement(DataObject periode,
			List<? extends DataObject> concurrents) {
		Date debut = (Date) periode.readProperty("debut");
		Date fin = (Date) periode.readProperty("fin");

		for (DataObject concurrent : concurrents) {
			if (concurrent == periode || concurrent.equals(periode))
				continue;

			Date debutConcurrent = (Date) concurrent.readProperty("debut");
			Date finConcurrent = (Date) concurrent.readProperty("fin");

			if (DateUtils.intersection(debut, fin, debutConcurrent,
					finConcurrent))
				return true;
		}
		return false;
	}

}
