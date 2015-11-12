package nc.ccas.gasel.model.core;

import static nc.ccas.gasel.modelUtils.CommonQueries.select;
import static org.apache.cayenne.DataObjectUtils.objectForPK;
import static org.apache.cayenne.DataObjectUtils.pkForObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectId;
import org.apache.cayenne.exp.Expression;

import com.asystan.common.Filter;
import com.asystan.common.lists.ListUtils;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.TraqueModifs;
import nc.ccas.gasel.model.aides.AspectAides;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.core.auto._Dossier;
import nc.ccas.gasel.model.habitat.AspectDossierHabitat;
import nc.ccas.gasel.model.habitat.AspectSIE;
import nc.ccas.gasel.model.paph.AspectDossierPAPH;
import nc.ccas.gasel.model.pe.AspectDossierAM;
import nc.ccas.gasel.model.pe.AspectDossierEnfant;
import nc.ccas.gasel.model.pi.AspectDossierPI;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.modelUtils.SqlUtils;

public class Dossier extends _Dossier implements ComplexDeletion, TraqueModifs {
	private static final long serialVersionUID = 8454370168358653226L;

	public static final Class<?>[] ASPECTS = new Class[] { AspectAides.class, AspectDossierAM.class,
			AspectDossierEnfant.class, AspectDossierHabitat.class, AspectDossierPAPH.class, AspectDossierPI.class,
			AspectSIE.class };

	public static final String aspectsActifsSql(String exprIdDossier, Collection<Class<?>> aspects) {
		StringBuilder buf = new StringBuilder("((");
		int idx = 0;
		for (Class<?> aspectClass : Dossier.ASPECTS) {
			String table = CayenneUtils.entityResolver().lookupObjEntity(aspectClass).getDbEntity()
					.getFullyQualifiedName();
			if (idx++ > 0)
				buf.append(") UNION (");
			buf.append("SELECT '" + aspectClass.getSimpleName() + "' FROM " + table + " WHERE dossier_id="
					+ exprIdDossier);
		}
		buf.append("))");
		return buf.toString();
	}

	public static String aspectsActifsSql(String exprIdDossier) {
		return aspectsActifsSql(exprIdDossier, Arrays.asList(ASPECTS));
	}

	public <T extends DataObject> T getAspect(Class<T> clazz) {
		if (getObjectId().isTemporary()) {
			// Impossible de chercher en base
			for (Object o : getObjectContext().newObjects()) {
				if (!clazz.isInstance(o))
					continue;
				T od = clazz.cast(o);
				if (od.readProperty("dossier") == this) {
					return od;
				}
			}
			return null;
		}
		return clazz.cast(objectForPK(getObjectContext(), clazz, pkForObject(this)));
	}

	@SuppressWarnings("unchecked")
	public DataObject getAspect(String className) {
		return getAspect(getObjectContext().getEntityResolver().getObjEntity(className).getJavaClass()
				.asSubclass(DataObject.class));
	}

	public int getTotalRessources() {
		return total(getRessources());
	}

	public int getTotalCharges() {
		return total(getCharges());
	}

	private int total(List<? extends RessCharge> valeurs) {
		int total = 0;
		if (valeurs == null) {
			return total;
		}
		for (RessCharge valeur : valeurs) {
			total += valeur.getMontant();
		}
		return total;
	}

	public List<Ressource> getRessources() {
		List<Ressource> ressources = new ArrayList<Ressource>();
		for (Personne person : getPersonnesRessources()) {
			if (person == null)
				continue;
			ressources.addAll(person.getRessources());
		}
		return ressources;
	}

	public List<Charge> getCharges() {
		List<Charge> charges = new ArrayList<Charge>();
		for (Personne person : getPersonnesRessources()) {
			if (person == null)
				continue;
			charges.addAll(person.getCharges());
		}
		return charges;
	}

	public int getNbPersonnes() {
		return getPersonnes().size();
	}

	public List<Personne> getPersonnes() {
		return personnes(true, true, true, true);
	}

	public List<Personne> getAdultes() {
		return ListUtils.filter(getPersonnes(), new Filter<Personne>() {
			public boolean accept(Personne p) {
				return p.getAge() >= 18;
			}
		});
	}

	public List<Personne> getEnfantsACharge() {
		return ListUtils.filter(getPersonnesACharge(), new Filter<Personne>() {
			public boolean accept(Personne p) {
				return p.getAge() < 18;
			}
		});
	}

	public int getNbPersonnesHandicapees() {
		int count = 0;
		for (Personne p : personnes(true, true, true, true)) {
			if (p.getHandicap())
				count++;
		}
		return count;
	}

	public List<Personne> getPersonnesHandicapees() {
		List<Personne> personnes = new ArrayList<Personne>();
		for (Personne p : personnes(true, true, true, true)) {
			if (p.getHandicap())
				personnes.add(p);
		}
		return personnes;
	}

	public List<Personne> getPersonnesRessources() {
		// return personnes(true, true, true, false);
		return personnes(true, true, true, true);
	}

	public List<Personne> getPersonnesAEtNonACharge() {
		return personnes(false, false, true, true);
	}

	private List<Personne> personnes(boolean chefFamille, boolean conjoint, boolean aCharge, boolean nonACharge) {
		List<Personne> ret = new ArrayList<Personne>();
		if (chefFamille && getChefFamille() != null) {
			ret.add(getChefFamille());
		}
		if (conjoint && getConjoint() != null) {
			ret.add(getConjoint());
		}
		if (aCharge) {
			ret.addAll(getPersonnesACharge());
		}
		if (nonACharge) {
			ret.addAll(getPersonnesNonACharge());
		}
		return ret;
	}

	@Override
	public String toString() {
		if (getChefFamille() == null)
			return "Anonyme";
		return getChefFamille().toString();
	}

	// ------------------------------------------------------------------
	// Gestion de la clé primaire
	//

	public Integer getId() {
		return SqlUtils.idOf(this);
	}

	@SuppressWarnings("unchecked")
	public void setId(int id) {
		ObjectId oid = getObjectId();
		if (oid != null && !oid.isTemporary())
			throw new IllegalStateException("ObjectID is not temporary");

		if (oid == null) {
			oid = new ObjectId(getObjectId().getEntityName());
		}
		oid.getReplacementIdMap().put("id", id);
	}

	public void initId() {
		setId(nextAvailableId());
	}

	private static int nextAvailableId() {
		final int MULT = (int) 1e4;

		int year = new GregorianCalendar().get(Calendar.YEAR);

		Integer currentMax = CommonQueries.max(Dossier.class, "id",
				"id >= " + year * MULT + " AND id < " + (year + 1) * MULT);

		int currentSequenceMax;
		if (currentMax == null) {
			currentSequenceMax = 0;
		} else {
			currentSequenceMax = currentMax % MULT;
		}

		return year * MULT + currentSequenceMax + 1;
	}

	/*
	 * --
	 */

	public void prepareForDeletion() {
		// Suppression des aspects
		for (Class<?> clazz : ASPECTS) {
			DataObject aspect = getAspect(clazz.asSubclass(DataObject.class));
			if (aspect == null)
				continue;
			DeletionUtils.delete(aspect);
		}

		// Des personnes
		Expression expr = Expression.fromString("personne = $personne and aide.dossier.dossier <> $dossier");
		Map<String, Object> parameters = new TreeMap<>();
		for (Personne personne : getPersonnes()) {
			// TODO check bons sur autres dossier
			parameters.put("personne", personne);
			parameters.put("dossier", this);

			// Recherche des bons sur la personne considérée et étant sur d'autres dossier
			List<Bon> bons = select(Bon.class, expr.expWithParameters(parameters));
			if (bons.isEmpty()) {
				// On ne peut supprimer que si la personne n'en a pas.
				DeletionUtils.delete(personne);
			}
		}

		// Des problématiques
		DeletionUtils.empty(this, PROBLEMATIQUES_PROPERTY);

		// Et des types de public
		DeletionUtils.empty(this, TYPES_PUBLIC_PROPERTY);
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.updateTraqueModifs(this, user, date);
	}

}
