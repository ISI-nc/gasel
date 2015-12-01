package nc.ccas.gasel.model.pe;

import static nc.ccas.gasel.modelUtils.DateUtils.intersection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.pe.auto._AssistanteMaternelle;
import nc.ccas.gasel.model.pe.enums.ModeGarde;

import org.apache.cayenne.DataObjectUtils;

@Feminin
public class AssistanteMaternelle extends _AssistanteMaternelle implements
		ComplexDeletion {

	private static final long serialVersionUID = -6842723889399660333L;

	public AssistanteMaternelle() {
		setNbEnfantsSouhaites(3);
	}

	// ------------------------------------------------------------------------
	// Gestion des gardes
	//

	public List<JourGardeAM> calendrier(Date debut, Date fin) {
		List<JourGardeAM> retval = new ArrayList<JourGardeAM>();
		List<Garde> gardes = getGardes();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(debut);
		for (; !cal.getTime().after(fin); cal.add(Calendar.DATE, 1)) {
			JourGardeAM jg = new JourGardeAM(cal.getTime());
			for (Garde garde : gardes) {
				if (!intersection(debut, fin, garde.getDebut(), garde.getFin()))
					continue;
				jg.merge(garde.getMode());
			}
			retval.add(jg);
		}
		return retval;
	}

	public Iterable<JourGardeAM> calendrierIt(Date debut, Date fin) {
		// TODO : optimiser avec un Iterable sur un Iterator
		// (et refactoriser calendrier pour l'utiliser)
		return calendrier(debut, fin);
	}

	public boolean peutPrendre(EnfantRAM enfant, ModeGarde mode, Date debut,
			Date fin) {
		int ponderation = enfant.getHandicap() == null ? 1 : enfant
				.getHandicap().getPonderation();
		int max = getNbEnfantsSouhaites();
		if (ponderation > max)
			return false;
		for (JourGardeAM jg : calendrier(debut, fin)) {
			int jour = jg.getJourSemaine();
			if (mode.garde(jour, JourGarde.MATIN)
					&& (jg.matin + ponderation) > max)
				return false;
			if (mode.garde(jour, JourGarde.MIDI)
					&& (jg.midi + ponderation) > max)
				return false;
			if (mode.garde(jour, JourGarde.APRES_MIDI)
					&& (jg.apresMidi + ponderation) > max)
				return false;
			if (mode.garde(jour, JourGarde.SOIR)
					&& (jg.soir + ponderation) > max)
				return false;
		}
		return true;
	}

	// ------------------------------------------------------------------------

	public Integer getId() {
		if (getObjectId().isTemporary())
			return null;
		return DataObjectUtils.intPKForObject(this);
	}

	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		return String.valueOf(getPersonne());
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getAbsencesAM());
		DeletionUtils.delete(getFormationsAM());
		DeletionUtils.delete(getGardes());
		DeletionUtils.delete(getIntegrations());
	}

}
