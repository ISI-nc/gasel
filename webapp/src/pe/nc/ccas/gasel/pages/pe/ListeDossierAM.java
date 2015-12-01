package nc.ccas.gasel.pages.pe;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nc.ccas.gasel.Check;
import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.pe.AspectDossierAM;
import nc.ccas.gasel.model.pe.AssistanteMaternelle;

public abstract class ListeDossierAM extends EditPage<AspectDossierAM> {

	static final int AGE_MIN = 18;

	public ListeDossierAM() {
		super(AspectDossierAM.class);
	}

	@Override
	protected void prepareEnregistrer() {
		Dossier dossier = getDossier();
		dossier.setModifDate(new Date());
	}

	public Dossier getDossier() {
		return getObject().getDossier();
	}

	public List<Personne> getAssistantes() {
		return sql.filtrer(getDossier().getPersonnes(), new AgeMinCheck());
	}

	public void editerDossierAM(Personne personne) {
		EditerDossierAM page = (EditerDossierAM) getRequestCycle().getPage(
				"pe/EditerDossierAM");
		AssistanteMaternelle am = findPersonne(personne);
		if (am == null) {
			page
					.activateForParent(getObject(), "dossier", "personne",
							personne);
		} else {
			page.openWithParent(am, "dossier");
		}
	}

	public AssistanteMaternelle findPersonne(Personne personne) {
		for (AssistanteMaternelle am : getObject().getAssistantesMaternelle()) {
			if (am.getPersonne().equals(personne)) {
				return am;
			}
		}
		return null;
	}
}

class AgeMinCheck implements Check<Personne> {

	private Date min;

	public AgeMinCheck() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.YEAR, -ListeDossierAM.AGE_MIN);
		min = cal.getTime();
	}

	public boolean check(Personne p) {
		return p.getDateNaissance().before(min);
	}

}
