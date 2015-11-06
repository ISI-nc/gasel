package nc.ccas.gasel.model.pe.enums;

import static nc.ccas.gasel.model.pe.JourGarde.JOURS;
import static nc.ccas.gasel.model.pe.JourGarde.PARTIES_JOURNEE;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.NoSuchElementException;

import nc.ccas.gasel.model.pe.JourGarde;
import nc.ccas.gasel.model.pe.enums.auto._ModeGarde;

public class ModeGarde extends _ModeGarde {

	private static final long serialVersionUID = 2955370304937779284L;

	/*
	 * Exploitation de la norme de nommage des booléens du mode de garde.
	 */

	private static final String[][] PROPS_GARDE = new String[JOURS.length + 1][PARTIES_JOURNEE.length];
	static {
		for (int jour : JOURS) {
			for (int partie : PARTIES_JOURNEE) {
				PROPS_GARDE[jour][partie] = propGarde(jour, partie);
			}
		}
	}

	private static String propGarde(int jour, int partie) {
		return prefixePropJour(jour) + suffixePropJour(partie);
	}

	private static String prefixePropJour(int jour) {
		switch (jour) {
		case Calendar.MONDAY:
			return "lundi";
		case Calendar.TUESDAY:
			return "mardi";
		case Calendar.WEDNESDAY:
			return "mercredi";
		case Calendar.THURSDAY:
			return "jeudi";
		case Calendar.FRIDAY:
			return "vendredi";
		case Calendar.SATURDAY:
			return "samedi";
		case Calendar.SUNDAY:
			return "dimanche";
		default:
			throw new IllegalArgumentException(String.valueOf(jour));
		}
	}

	private static String suffixePropJour(int partie) {
		switch (partie) {
		case JourGarde.MATIN:
			return "Matin";
		case JourGarde.MIDI:
			return "Midi";
		case JourGarde.APRES_MIDI:
			return "AM";
		case JourGarde.SOIR:
			return "Soir";
		default:
			throw new IllegalArgumentException(String.valueOf(partie));
		}
	}

	// ------------------------------------------------------------------------

	public ModeGarde() {
		// On ne coche
		for (int jour : JOURS) {
			for (int partie : PARTIES_JOURNEE) {
				set(jour, partie, false);
			}
		}
	}

	@Override
	public String toString() {
		return getLibelle();
	}

	public boolean gardeMatin(int jourSemaine) {
		return garde(jourSemaine, JourGarde.MATIN);
	}

	public boolean gardeMidi(int jourSemaine) {
		return garde(jourSemaine, JourGarde.MIDI);
	}

	public boolean gardeAM(int jourSemaine) {
		return garde(jourSemaine, JourGarde.APRES_MIDI);
	}

	public boolean gardeSoir(int jourSemaine) {
		return garde(jourSemaine, JourGarde.SOIR);
	}

	public void setPartieJournee(int partieJournee, boolean lundi,
			boolean mardi, boolean mercredi, boolean jeudi, boolean vendredi,
			boolean samedi, boolean dimanche) {
		set(Calendar.MONDAY, partieJournee, lundi);
		set(Calendar.TUESDAY, partieJournee, mardi);
		set(Calendar.WEDNESDAY, partieJournee, mercredi);
		set(Calendar.THURSDAY, partieJournee, jeudi);
		set(Calendar.FRIDAY, partieJournee, vendredi);
		set(Calendar.SATURDAY, partieJournee, samedi);
		set(Calendar.SUNDAY, partieJournee, dimanche);
	}

	public void setJournee(int jourSemaine, boolean matin, boolean midi,
			boolean apresMidi, boolean soir) {
		set(jourSemaine, JourGarde.MATIN, matin);
		set(jourSemaine, JourGarde.MIDI, midi);
		set(jourSemaine, JourGarde.APRES_MIDI, apresMidi);
		set(jourSemaine, JourGarde.SOIR, soir);
	}

	public boolean garde(int jourSemaine, int partieJournee) {
		try {
			return (Boolean) readProperty(propGarde(jourSemaine, partieJournee));
		} catch (NullPointerException ex) {
			throw new NullPointerException(String.valueOf(propGarde(
					jourSemaine, partieJournee)));
		}
	}

	public void set(int jour, int partie, boolean value) {
		writeProperty(propGarde(jour, partie), value);
	}

	public Iterable<JourGarde> developpement(Date debut, Date fin) {
		return getDeveloppement(debut, fin);
	}

	public Iterable<JourGarde> getDeveloppement(Date debut, Date fin) {
		return new JourGardeIterable(debut, fin);
	}

	private class JourGardeIterable implements Iterable<JourGarde> {
		private final Date debut;

		private final Date fin;

		public JourGardeIterable(Date debut, Date fin) {
			this.debut = debut;
			this.fin = fin;
		}

		public Iterator<JourGarde> iterator() {
			return new JourGardeIterator(debut, fin);
		}
	}

	private class JourGardeIterator implements Iterator<JourGarde> {

		private final GregorianCalendar current = new GregorianCalendar();

		private final Date fin;

		public JourGardeIterator(Date debut, Date fin) {
			current.setTime(debut);
			this.fin = fin;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public JourGarde next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Date date = current.getTime();
			int jourSemaine = current.get(Calendar.DAY_OF_WEEK);
			current.add(Calendar.DATE, 1);
			return new JourGarde(date, gardeMatin(jourSemaine),
					gardeMidi(jourSemaine), gardeAM(jourSemaine),
					gardeSoir(jourSemaine));
		}

		public boolean hasNext() {
			return !current.getTime().after(fin);
		}

	}

}
