package nc.ccas.gasel.pages.aides;

import static com.asystan.common.cayenne.QueryFactory.createBetween;
import static nc.ccas.gasel.modelUtils.DateUtils.intersection;

import java.util.Date;
import java.util.List;

import nc.ccas.gasel.Check;
import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.AideRefusee;
import nc.ccas.gasel.model.aides.AspectAides;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageEvent;

public abstract class ListeAidesDossier extends EditPage<AspectAides> implements
		PeriodeProps {

	public ListeAidesDossier() {
		super(AspectAides.class);
	}

	public Date getDefaultPeriodeDebut() {
		return dates.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return dates.finAnnee();
	}

	@Override
	protected void prepareEnregistrer() {
		Dossier dossier = getDossier();
		dossier.setModifDate(new Date());
	}

	public Dossier getDossier() {
		if (getParent() != null) {
			return (Dossier) getParent();
		}
		return getObject().getDossier();
	}

	public void setDossier(Dossier dossier) {
		setParent(dossier);
		getObject().setDossier(dossier);
	}

	public List<Aide> getAides() {
		return sql.filtrer(getObject().getAides(), new Check<Aide>() {
			public boolean check(Aide value) {
				return intersection( //
						getPeriodeDebut(), getPeriodeFin(), //
						value.getDebut(), value.getFin());
			}
		});
	}

	public List<AideRefusee> getAidesRefusees() {
		Expression expr = createBetween("demande", getPeriodeDebut(),
				getPeriodeFin());
		return sql.filtrer(getObject().getAidesRefusees(), expr);
	}

	public void ajouterAide(IRequestCycle cycle) {
		GestionAide page = (GestionAide) cycle.getPage("aides/GestionAide");
		page.activateForParent(getObject(), "dossier");
	}

	// Aides refusées

	@Persist("workflow")
	public abstract AideRefusee getRefus();

	public abstract void setRefus(AideRefusee refus);

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		if (getRefus() == null)
			setRefus(new AideRefusee());
	}

}
