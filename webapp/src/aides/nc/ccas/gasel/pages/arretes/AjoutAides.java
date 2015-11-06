package nc.ccas.gasel.pages.arretes;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.aides.Arrete;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;
import nc.ccas.gasel.model.aides.TypeArrete;
import nc.ccas.gasel.model.aides.UsageBon;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.workflow.WorkflowEntry;

import org.apache.cayenne.access.DataContext;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageEvent;

public abstract class AjoutAides extends BasePage {

	@Persist("workflow")
	public abstract TypeArrete getTypeArrete();

	public abstract void setTypeArrete(TypeArrete typeArrete);

	@Persist("workflow")
	public abstract Date getMois();

	@Persist("workflow")
	public abstract Set<AjoutAidesLigne> getSelection();

	public abstract void setSelection(Set<AjoutAidesLigne> selection);

	public abstract List<Bon> getResults();

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		if (getSelection() == null) {
			setSelection(new HashSet<AjoutAidesLigne>());
		}
	}

	public List<AjoutAidesLigne> traite(List<Bon> results) {
		return AjoutAidesLigne.traite(results);
	}

	public void ajout(IRequestCycle cycle) {
		Set<AjoutAidesLigne> selection = getSelection();

		ajoutLignes(cycle, selection);
	}

	public void toutAjouter(IRequestCycle cycle) {
		ajoutLignes(cycle, traite(getResults()));
	}

	private void ajoutLignes(IRequestCycle cycle,
			Collection<AjoutAidesLigne> lignes) {
		WorkflowEntry entry = getWorkflow().getCurrentEntry();
		WorkflowEntry parentEntry = entry.getParent();

		// On travaille sur l'entrée parente
		getWorkflow().setCurrentEntry(parentEntry);
		DataContext dc = parentEntry.getObjectContext();

		Creation page = (Creation) cycle.getPage(parentEntry.getPageName());
		Arrete arrete = page.getObject();
		for (AjoutAidesLigne aj : lignes) {
			for (Bon bon : aj.getBons()) {
				UsageBon usage = bon.getUsage();
				usage = (UsageBon) dc.localObject(usage.getObjectId(), usage);
				arrete.addToBonsValides(usage);
			}
		}
		page.dontRedirect();
		page.sauver();

		// Retour sur notre entrée et fermeture
		getWorkflow().close(entry, cycle, false);
		redirect(cycle);
	}

	public String getFilterExpression() {
		StringBuilder expr = new StringBuilder();

		expr.append("(usages.arrete = null) and ");
		expr.append("(usages.facture != null) and");
		expr.append("(etat = " + EtatBon.UTILISE + " or etat = "
				+ EtatBon.PARTIELLEMENT_UTILISE + ") and");
		expr.append("(aide.nature.typeArrete = "+getTypeArrete().getId()+")");

		return expr.toString();
	}

}
