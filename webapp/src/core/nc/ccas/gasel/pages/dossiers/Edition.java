package nc.ccas.gasel.pages.dossiers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import nc.ccas.gasel.BasePageDates;
import nc.ccas.gasel.Check;
import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.Utils;
import nc.ccas.gasel.jwcs.core.Tableau;
import nc.ccas.gasel.model.core.Adresse;
import nc.ccas.gasel.model.core.Charge;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.core.RessCharge;
import nc.ccas.gasel.model.core.Ressource;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.core.enums.OrigineSignalement;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.cayenne.DataObject;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * 
 * @author Mikaël Cluseau - ISI.NC
 * 
 */
public abstract class Edition extends EditPage<Dossier> implements PeriodeProps {

	static <T extends RessCharge> List<T> filtrerRessCharges(List<T> values,
			Date debut, Date fin, BasePageDates dates) {
		List<T> retval = new ArrayList<T>(values.size());
		for (T rc : values) {
			if (!dates.checkPeriode(debut, fin, rc.getDebut(), rc.getFin()))
				continue;
			retval.add(rc);
		}
		return retval;
	}

	public Edition() {
		super(Dossier.class);
	}

	public Date getDefaultPeriodeDebut() {
		return dates.debutMois();
	}

	public Date getDefaultPeriodeFin() {
		return dates.finMois();
	}

	public IPropertySelectionModel getModeleReferent() {
		Collection<Utilisateur> utilisateurs = sql.query() //
				.all(Utilisateur.class);

		utilisateurs = sort(utilisateurs).byToString().results();
		return psm.withNull(psm.dataObject(utilisateurs));
	}

	// Personnes à charge ou non

	@InjectPage("dossiers/PersonneSearch")
	public abstract PersonneSearch getPersonneSearchPage();

	public void ajouterACharge(IRequestCycle cycle) {
		getPersonneSearchPage().activate(this, "retourAjoutACharge");
	}

	public void retourAjoutACharge(Personne p) {
		getDossier().addToPersonnesACharge(p);
	}

	public void delACharge(Personne p) {
		getDossier().removeFromPersonnesACharge(p);
		redirect(getRequestCycle());
	}

	public void ajouterNonACharge(IRequestCycle cycle) {
		getPersonneSearchPage().activate(this, "retourAjoutNonACharge");
	}

	public void retourAjoutNonACharge(Personne p) {
		getDossier().addToPersonnesNonACharge(p);
	}

	public void delNonACharge(Personne p) {
		getDossier().removeFromPersonnesNonACharge(p);
		redirect(getRequestCycle());
	}

	// Ressources et charges

	public IPropertySelectionModel getPersonnesDossierPSM() {
		return psm.dataObject(getDossier().getPersonnesRessources());
	}

	public void ressourceAdded() {
		((Tableau) getComponent("tableauRessources")).showLastPage();
		redirect();
	}

	public void delRessource(Ressource res) {
		res.getPersonne().removeFromRessources(res);
		getObjectContext().deleteObject(res);
		redirect();
	}

	public void chargeAdded() {
		((Tableau) getComponent("tableauCharges")).showLastPage();
		redirect();
	}

	public void delCharge(Charge res) {
		res.getPersonne().removeFromCharges(res);
		getObjectContext().deleteObject(res);
		redirect();
	}

	//

	@Override
	public void prepareNewObject(Dossier dossier) {
		validate(getRequestCycle());
		dossier.setAdresseHabitation(createDataObject(Adresse.class)); // Obligatoire
		dossier.setDateOuverture(Utils.today());
		dossier.setReferent(ensureDataContext(getLogin().getUser()));
		dossier.setSignalement(sql.query().byId(OrigineSignalement.class,
				OrigineSignalement.SPONTANE));
	}

	@Override
	protected void prepareCommit() {
		Dossier dossier = getDossier();
		if (dossier.getObjectId().isTemporary())
			dossier.initId();
	}

	public <T extends RessCharge> List<T> filtrerRessCharge(List<T> values) {
		return filtrerRessCharges(values, getPeriodeDebut(), getPeriodeFin(),
				dates);
	}

	public double totalRessCharge(List<? extends RessCharge> values) {
		int total = 0;
		for (RessCharge res : filtrerRessCharge(values)) {
			total += res.getMontant();
		}
		return total;
	}

	/**
	 * Alias pour getObject
	 */
	public Dossier getDossier() {
		return getObject();
	}

	/**
	 * Alias pour setObject
	 */
	public void setDossier(Dossier dossier) {
		setObject(dossier);
	}

	@Override
	public String getTitre() {
		if (getDossier() == null) {
			return "?";
		}
		StringBuffer buf = new StringBuffer("Dossier ");
		if (getDossier().getChefFamille() == null) {
			buf.append("?");
		} else {
			buf.append(getDossier().getChefFamille().getRepr());
		}
		buf.append(objectStatus());
		return buf.toString();
	}

	// ------------------------------------------------------------------------
	// Gestion des aspects
	//

	public abstract List<List<String>> getAspects();

	@SuppressWarnings("unchecked")
	public <T extends DataObject> void openAspect(IRequestCycle cycle,
			String objEntityName) {
		// Récup de la classe
		Class<T> clazz = (Class<T>) classForEntity(objEntityName);

		// Récup de la page
		String pageName = null;
		for (List<String> specAspect : getAspects()) {
			if (specAspect.get(1).equals(objEntityName)) {
				pageName = specAspect.get(2);
				break;
			}
		}
		if (pageName == null) {
			throw new RuntimeException(pageName);
		}

		EditPage<T> page = (EditPage<T>) cycle.getPage(pageName);

		// Ouverture
		openAspect(clazz, page);
	}

	private Class<? extends DataObject> classForEntity(String objEntityName) {
		return getObjectContext().getEntityResolver().getObjEntity(objEntityName)
				.getJavaClass().asSubclass(DataObject.class);
	}

	private <T extends DataObject> void openAspect(Class<T> classeAspect,
			EditPage<T> page) {
		T aspect = getObject().getAspect(classeAspect);
		if (aspect == null) {
			page.activateForParent(getObject(), "dossier");
		} else {
			page.openWithParent(aspect, "dossier");
		}
	}

	public List<List<String>> getAspectsActifs() {
		return lists.filter(getAspects(), new Check<List<String>>() {
			public boolean check(List<String> spec) {
				return getObject().getAspect(classForEntity(spec.get(1))) != null;
			}
		});
	}

	public List<List<String>> getAspectsInactifs() {
		return lists.filter(getAspects(), new Check<List<String>>() {
			public boolean check(List<String> spec) {
				return getObject().getAspect(classForEntity(spec.get(1))) == null;
			}
		});
	}

}
