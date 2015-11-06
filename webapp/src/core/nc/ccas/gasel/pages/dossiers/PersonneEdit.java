package nc.ccas.gasel.pages.dossiers;

import static nc.ccas.gasel.pages.dossiers.Edition.filtrerRessCharges;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.core.ActivitePersonne;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.core.RessCharge;
import nc.ccas.gasel.model.core.Ressource;
import nc.ccas.gasel.model.core.enums.TypeRessource;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.cayenne.query.Ordering;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;

/**
 * 
 * @author Mikaël Cluseau - ISI.NC
 * 
 */
public abstract class PersonneEdit extends EditPage<Personne> implements
		PeriodeProps {

	public PersonneEdit() {
		super(Personne.class);
	}

	public Date getDefaultPeriodeDebut() {
		return dates.debutMois();
	}

	public Date getDefaultPeriodeFin() {
		return dates.finMois();
	}

	@InjectPage("dossiers/ActivitePersonneEdit")
	public abstract ActivitePersonneEdit getActiviteEditPage();

	public void ajouterActivite(IRequestCycle cycle) {
		getActiviteEditPage().activate(getPersonne());
	}

	@Override
	public void prepareNewObject(Personne object) {
		object.setSexe(true);
		object.setDesignation("M");
		object.setNationaliteFrancaise(true);
		object.setNeEtranger(false);
		object.setHandicap(false);

		String nom = (String) wf.componentValue(PersonneSearch.class, "nomCF",
				"value");
		if (nom == null) {
			nom = (String) wf.componentValue(Recherche.class, "nomCF", "value");
		}
		object.setNom(nom);
	}

	@Override
	protected void prepareEnregistrer() {
		Personne p = getObject();
		// Si décédé, on supprime les ressources post-décès.
		if (p.getDateDeces() != null) {
			Date deces = p.getDateDeces();
			Set<Ressource> aVirer = new HashSet<Ressource>();
			for (Ressource res : p.getRessources()) {
				if (res.getDebut().after(deces)) {
					aVirer.add(res);
				} else if (res.getFin().after(deces)) {
					res.setFin(deces);
				}
			}
			for (Ressource res : aVirer) {
				p.getRessources().remove(res);
				getObjectContext().deleteObject(res);
			}
		}
	}

	// XXX duplicata Edition
	public <T extends RessCharge> List<T> filtrerRessCharge(List<T> values) {
		return filtrerRessCharges(values, getPeriodeDebut(), getPeriodeFin(),
				dates);
	}

	// XXX duplicata Edition
	public int totalRessCharge(List<? extends RessCharge> values) {
		int total = 0;
		for (RessCharge res : filtrerRessCharge(values)) {
			total += res.getMontant();
		}
		return total;
	}

	@Override
	public String getTitre() {
		if (getObject() == null) {
			return "?";
		}
		return getObject().getRepr() + " " + objectStatus();
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_activites = null;
	}

	private List<ActivitePersonne> _activites;

	public List<ActivitePersonne> getActivites() {
		if (_activites == null) {
			_activites = getPersonne().getActivites();
			Collections.sort(_activites, new Ordering("fin", false));
			Collections.sort(_activites, new Ordering("debut", false));
		}
		return _activites;
	}

	public abstract List<Ressource> getRessources();

	public int getTotalRessources() {
		int total = 0;
		for (Ressource res : getRessources()) {
			if (res.getMontant() == null)
				continue;
			total += res.getMontant();
		}
		return total;
	}

	public abstract TypeRessource getAjResType();

	public abstract Number getAjResMontant();

	public abstract void setAjResMontant(Number value);

	// Alias pour getObject
	public Personne getPersonne() {
		return getObject();
	}

	public void setPersonne(Personne p) {
		setObject(p);
	}

}
