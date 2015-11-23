package nc.ccas.gasel.model.habitat;

import java.util.Date;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.habitat.auto._AspectDossierHabitat;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.modelUtils.ListUtils;

import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;

import com.asystan.common.cayenne_new.QueryFactory;

public class AspectDossierHabitat extends _AspectDossierHabitat implements
		ComplexDeletion, ModifListener {
	private static final long serialVersionUID = -6236832090634776759L;

	public DemandeAffectation getDerniereDemandeAffectation() {
		return (DemandeAffectation) ListUtils.max(this, "demandesAffectation",
				"date");
	}

	public int getALProvince() {
		int total = 0;
		for (DemandeAideLogement demande : getDemandesAideLogement()) {
			for (AideLogement aide : demande.getAides()) {
				total += aide.getMontantAL();
			}
		}
		return total;
	}

	public AideLogement getPremiereAide() {
		SelectQuery query = queryAides();
		query.addOrdering("debut", true);
		return (AideLogement) CommonQueries.unique(getObjectContext(), query);
	}

	public AideLogement getDerniereAide() {
		SelectQuery query = queryAides();
		query.addOrdering("fin", false);
		return (AideLogement) CommonQueries.unique(getObjectContext(), query);
	}

	private SelectQuery queryAides() {
		Expression expr = QueryFactory.createEquals("demande.dossierHabitat",
				this);
		SelectQuery query = new SelectQuery(AideLogement.class, expr);
		return query;
	}

	public SecoursImmediatExceptionnel getMontantSIE() {
		SelectQuery query = querySIE();
		query.addOrdering("montant", false);
		return (SecoursImmediatExceptionnel) CommonQueries.unique(
				getObjectContext(), query);
	}

	private SelectQuery querySIE() {
		Expression expr = QueryFactory.createEquals("dossier", getDossier());
		SelectQuery query = new SelectQuery(AspectSIE.class, expr);
		return query;
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getAides());
		DeletionUtils.delete(getAidesComplementaires());
		DeletionUtils.delete(getAutresProblemes());
		DeletionUtils.delete(getDemandesAffectation());
		DeletionUtils.delete(getDemandesAideLogement());
		DeletionUtils.delete(getPlanning());
		
		DeletionUtils.empty(this, AspectDossierHabitat.AIDES_COMPLEMENTAIRES_PROPERTY);
		DeletionUtils.empty(this, AspectDossierHabitat.PLANNING_PROPERTY);
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDossier());
	}

}
