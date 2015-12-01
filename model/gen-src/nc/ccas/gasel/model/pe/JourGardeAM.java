package nc.ccas.gasel.model.pe;

import static java.lang.String.valueOf;
import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import nc.ccas.gasel.model.pe.enums.ModeGarde;

public class JourGardeAM {

	public static final int MATIN = 0;

	public static final int MIDI = 1;

	public static final int APRES_MIDI = 2;

	public static final int SOIR = 3;

	public static final int[] PARTIES_JOURNEE = new int[] { MATIN, MIDI,
			APRES_MIDI, SOIR };

	public static final int[] JOURS = new int[] { MONDAY, TUESDAY, WEDNESDAY,
			THURSDAY, FRIDAY, SATURDAY, SUNDAY };

	// ------------------------------------------------------------------------

	public final Date date;

	private int jourSemaine;

	public int matin = 0;

	public int midi = 0;

	public int apresMidi = 0;

	public int soir = 0;

	public JourGardeAM(Date date) {
		this.date = date;
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		jourSemaine = gc.get(Calendar.DAY_OF_WEEK);
	}

	public void merge(ModeGarde mode) {
		if (mode.garde(jourSemaine, MATIN))
			matin++;
		if (mode.garde(jourSemaine, MIDI))
			midi++;
		if (mode.garde(jourSemaine, APRES_MIDI))
			apresMidi++;
		if (mode.garde(jourSemaine, SOIR))
			soir++;
	}

	@Override
	public String toString() {
		return "<" + date + ": matin=" + valueOf(matin) + ", midi="
				+ valueOf(midi) + ", AM=" + valueOf(apresMidi) + ", soir="
				+ valueOf(soir) + ">";
	}

	public int getJourSemaine() {
		return jourSemaine;
	}

	public int get(int partieJournee) {
		switch (partieJournee) {
		case MATIN:
			return matin;
		case MIDI:
			return midi;
		case APRES_MIDI:
			return apresMidi;
		case SOIR:
			return soir;
		default:
			throw new IllegalArgumentException(String.valueOf(partieJournee));
		}
	}

}
