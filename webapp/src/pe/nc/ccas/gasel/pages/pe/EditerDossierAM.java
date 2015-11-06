package nc.ccas.gasel.pages.pe;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.WEEK_OF_YEAR;
import static nc.ccas.gasel.model.pe.JourGarde.PARTIES_JOURNEE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pe.AssistanteMaternelle;
import nc.ccas.gasel.model.pe.JourGardeAM;
import nc.ccas.gasel.reports.PeriodeProps;

public abstract class EditerDossierAM extends EditPage<AssistanteMaternelle>
		implements PeriodeProps {

	public EditerDossierAM() {
		super(AssistanteMaternelle.class);
	}

	public List<LigneCalendrier> getLignesSemaine() {
		List<LigneCalendrier> retval = new ArrayList<LigneCalendrier>();

		GregorianCalendar curWeek = new GregorianCalendar();
		curWeek.setTime(getPeriodeDebut());
		curWeek.set(DAY_OF_WEEK, Calendar.MONDAY);

		GregorianCalendar fin = new GregorianCalendar();
		fin.setTime(getPeriodeFin());
		fin.set(DAY_OF_WEEK, Calendar.MONDAY);
		fin.add(DATE, 6);

		for (; !curWeek.after(fin); curWeek.add(DATE, 7)) {
			GregorianCalendar firstDay = new GregorianCalendar();
			firstDay.setTime(curWeek.getTime());

			GregorianCalendar lastDay = new GregorianCalendar();
			lastDay.setTime(firstDay.getTime());
			lastDay.add(DATE, 6);

			List<JourGardeAM> calSemaine = getAssistante().calendrier(
					firstDay.getTime(), lastDay.getTime());

			int semaine = firstDay.get(WEEK_OF_YEAR);
			retval.add(new LigneCalendrier(true, "Sem. " + semaine));

			for (int partieJournee : PARTIES_JOURNEE) {
				LigneCalendrier ligne = new LigneCalendrier(false,
						getMessages().getMessage("partie." + partieJournee));

				ligne.setLundi(calSemaine.get(0).get(partieJournee));
				ligne.setMardi(calSemaine.get(1).get(partieJournee));
				ligne.setMercredi(calSemaine.get(2).get(partieJournee));
				ligne.setJeudi(calSemaine.get(3).get(partieJournee));
				ligne.setVendredi(calSemaine.get(4).get(partieJournee));
				ligne.setSamedi(calSemaine.get(5).get(partieJournee));
				ligne.setDimanche(calSemaine.get(6).get(partieJournee));

				retval.add(ligne);
			}
		}
		return retval;
	}

	public AssistanteMaternelle getAssistante() {
		return getObject();
	}

	public void setAssistante(AssistanteMaternelle am) {
		setObject(am);
	}

	@Override
	public String getTitre() {
		if (getAssistante() == null) {
			return "AM";
		}
		return "AM: " + getAssistante().getPersonne();
	}

	public Date getDefaultPeriodeDebut() {
		return dates.debutMois();
	}

	public Date getDefaultPeriodeFin() {
		return dates.finMois();
	}

}
