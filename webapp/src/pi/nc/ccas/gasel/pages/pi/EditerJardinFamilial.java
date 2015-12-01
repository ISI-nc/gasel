package nc.ccas.gasel.pages.pi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.pi.AttributionJF;
import nc.ccas.gasel.model.pi.DemandeJF;
import nc.ccas.gasel.model.pi.JardinFamilial;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.tapestry.annotations.InjectPage;

public abstract class EditerJardinFamilial extends EditPage<JardinFamilial>
		implements PeriodeProps {

	public EditerJardinFamilial() {
		super(JardinFamilial.class);
	}

	public Date getDefaultPeriodeDebut() {
		return dates.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return dates.finAnnee();
	}

	public void setJardin(JardinFamilial jardin) {
		setObject(jardin);
	}

	public JardinFamilial getJardin() {
		return getObject();
	}

	@InjectPage("pi/EditerAttribution")
	public abstract EditerAttribution getPageAttribution();

	public void attribution(DemandeJF demande) {
		getPageAttribution().activateForParent(demande, "demande");
	}

	public List<AttributionJF> getAttributionsPeriode() {
		List<AttributionJF> attrs = getObject().getAttributions();
		List<AttributionJF> retval = new ArrayList<AttributionJF>(attrs.size());
		for (AttributionJF attr : attrs) {
			if (!dates.checkPeriode(getPeriodeDebut(), getPeriodeFin(), //
					attr.getDate(), attr.getDate()))
				continue;
			retval.add(attr);
		}
		return retval;
	}

	@Override
	public String getTitre() {
		if (getObject() == null) {
			return "JF";
		}
		return "JF: " + getObject();
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

	public void editerParcelle(JardinFamilial jardin) {
		((EditerParcelle) getRequestCycle().getPage("pi/EditerParcelle"))
				.activateForParent(jardin, "jardin");
	}

	public Boolean estEnCours(AttributionJF att) {
		return ((!att.isTerminee()) && (att.hasPaiementEnCours()));
	}

}
