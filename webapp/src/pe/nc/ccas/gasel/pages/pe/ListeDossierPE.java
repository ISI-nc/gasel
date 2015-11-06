package nc.ccas.gasel.pages.pe;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nc.ccas.gasel.Check;
import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.pe.AspectDossierEnfant;
import nc.ccas.gasel.model.pe.EnfantRAM;

public abstract class ListeDossierPE extends EditPage<AspectDossierEnfant> {

	static final int AGE_MAX = 6;

	public ListeDossierPE() {
		super(AspectDossierEnfant.class);
	}

	@Override
	protected void prepareEnregistrer() {
		Dossier dossier = getDossier();
		dossier.setModifDate(new Date());
	}

	public Dossier getDossier() {
		return (Dossier) getParent();
		// getObject().getDossier();
	}

	public List<Personne> getEnfants() {
		// Ruby :
		// dossier.personnes_a_et_non_a_charge.find_all{|x|x.date_naissance >=
		// min}
		return sql.filtrer(getDossier().getPersonnesAEtNonACharge(),
				new AgeCheck());
	}

	public void editerDossierPE(Personne personne) {
		EditerDossierPE page = (EditerDossierPE) getRequestCycle().getPage(
				"pe/EditerDossierPE");
		// Recherche de l'enfant
		EnfantRAM enfant = getObject().findEnfantRAM(personne);
		if (enfant == null) {
			page.activateForParent( //
					getObject(), "dossier", "personne", personne);
		} else {
			page.openWithParent(enfant, "dossier");
		}
	}

}

class AgeCheck implements Check<Personne> {

	private Date min;

	public AgeCheck() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.YEAR, -ListeDossierPE.AGE_MAX);
		min = cal.getTime();
	}

	public boolean check(Personne p) {
		return p.getDateNaissance().after(min);
	}

}