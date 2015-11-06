package nc.ccas.gasel.model.pe;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

import java.util.Date;

public class JourGarde {

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

	public final boolean matin;

	public final boolean midi;

	public final boolean apresMidi;

	public final boolean soir;

	public JourGarde(Date date, boolean matin, boolean midi, boolean apresMidi,
			boolean soir) {
		this.date = date;
		this.matin = matin;
		this.midi = midi;
		this.apresMidi = apresMidi;
		this.soir = soir;
	}

	@Override
	public String toString() {
		return "<" + date + ": matin=" + reprBool(matin) + ", midi="
				+ reprBool(midi) + ", AM=" + reprBool(apresMidi) + ", soir="
				+ reprBool(soir) + ">";
	}

	private String reprBool(boolean b) {
		return (b ? "oui" : "non");
	}

}
