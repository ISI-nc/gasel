package nc.ccas.gasel.pages.pi;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pi.AspectDossierPI;
import nc.ccas.gasel.model.pi.AttributionJF;
import nc.ccas.gasel.model.pi.DemandeJF;

import org.apache.tapestry.event.PageEvent;

public abstract class EditerAspectPI extends EditPage<AspectDossierPI> {

	public EditerAspectPI() {
		super(AspectDossierPI.class);
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);

		if (getParent() == null) {
			setParentProperty("dossier");
			setParent(getObject().getDossier());
		}
	}

	@Override
	protected void prepareNewObject(AspectDossierPI pi) {
		if (pi == null) {
			throw new Error("Impossible de créer un aspect PI");
		}
	}
	
	public void reaffecterReferent() {
		getObject().setReferentFamilial(getLogin().getUser());
	}

	public void ajouterAttribution(DemandeJF demande) {
		((EditerAttribution) getRequestCycle().getPage("pi/EditerAttribution"))
				.activateForParent(demande, "demande");
	}

	public Boolean estActif(DemandeJF demande) {
		if (!demande.getAttributions().isEmpty())
			return false; // Attribuée

		Date dateDemande = demande.getDate();
		if (dateDemande == null)
			return false;
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.MONTH, -6);
		Date dateLimite = cal.getTime();
		return dateDemande.after(dateLimite);
	}

	public List<AttributionJF> getAttributions() {
		return sort(
				sql.query(AttributionJF.class, "demande.dossier="
						+ sql.idOf(getObject().getDossier()))) //
				.by("date", "desc") //
				.by("demande.date", "desc") //
				.results();
	}

}
