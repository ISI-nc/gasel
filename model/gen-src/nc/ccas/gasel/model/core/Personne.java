package nc.ccas.gasel.model.core;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import java_gaps.ComparisonChain;
import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.auto._Personne;
import nc.ccas.gasel.model.mairie.Commune;
import nc.ccas.gasel.model.mairie.Ville;
import nc.ccas.gasel.model.mairie.VilleEtrangere;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.PersistenceState;

import com.asystan.common.AutoBox;

@Feminin
public class Personne extends _Personne implements ComplexDeletion,
		ModifListener, Comparable<Personne> {
	private static final long serialVersionUID = -4087639573731722450L;

	private static final TreeMap<String, String> DESIGNATIONS_LONGUES = new TreeMap<String, String>();
	static {
		DESIGNATIONS_LONGUES.put("Mr", "Monsieur");
		DESIGNATIONS_LONGUES.put("M", "Monsieur");
		DESIGNATIONS_LONGUES.put("Mme", "Madame");
		DESIGNATIONS_LONGUES.put("Mlle", "Mademoiselle");
	}

	public <T extends DataObject> T getAspect(Class<T> clazz) {
		if (getPersistenceState() == PersistenceState.NEW) {
			// Impossible de chercher en base
			for (Object o : getObjectContext().newObjects()) {
				if (!clazz.isInstance(o))
					continue;
				T od = clazz.cast(o);
				if (od.readProperty("personne") == this) {
					return od;
				}
			}
			return null;
		}
		return clazz.cast(DataObjectUtils.objectForPK(getObjectContext(), clazz,
				(Integer) DataObjectUtils.pkForObject(this)));
	}

	/**
	 * @return Représentation type "M. Jean Dupond"
	 */
	public String getRepr() {
		return (getDesignation() != null ? getDesignation() + " " : "")
				+ getNomCommun() + ", " + getPrenom();
	}

	public ActivitePersonne getDerniereActivite() {
		ActivitePersonne ap = null;
		for (ActivitePersonne activite : getActivites()) {
			if (ap == null || activite.getDebut().after(ap.getDebut())) {
				ap = activite;
			}
		}
		return ap;
	}

	public Boolean isHomme() {
		return getSexe();
	}

	public Boolean isFemme() {
		return !AutoBox.valueOf(getSexe());
	}

	public String getNomCommun() {
		if (getNom() == null) {
			return getNomJeuneFille();
		}
		return getNom();
	}

	public Integer getSalaire() {
		for (Ressource res : getRessources()) {
			if (!res.getType().isSalaire())
				continue;

			return res.getMontant();
		}
		return null;
	}

	public int getAge() {
		GregorianCalendar anniv = gc(getDateNaissance());
		GregorianCalendar today = gc(DateUtils.today());

		int age = today.get(Calendar.YEAR) - anniv.get(Calendar.YEAR);

		anniv.set(Calendar.YEAR, today.get(Calendar.YEAR));
		if (anniv.compareTo(today) >= 0) {
			age++;
		}

		return age;
	}

	private GregorianCalendar gc(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc;
	}

	@Override
	public String toString() {
		return getRepr();
	}

	// Gestion de la ville de naissance

	public Ville getVilleNaissance() {
		if (getNeEtranger()) {
			return getNeVilleEtrangere();
		} else {
			return getNeVille();
		}
	}

	public void setVilleNaissance(Ville ville) {
		if (ville == null) {
			setNeVilleEtrangere(null);
			setNeVille(null);
			setNeEtranger(false);

		} else if (ville instanceof Commune) {
			setNeVille((Commune) ville);

		} else if (ville instanceof VilleEtrangere) {
			setNeVille(null);
			setNeVilleEtrangere((VilleEtrangere) ville);
			setNeEtranger(true);

		} else {
			throw new IllegalArgumentException("ville: " + ville.getClass());
		}
	}

	public void setNeVille(Commune commune) {
		super.setNeVille(commune);
		if (commune != null) {
			setNeVilleEtrangere(null);
			setNeEtranger(false);
		}
	}

	public void setNeVilleEtrangere(VilleEtrangere ville) {
		super.setNeVilleEtrangere(ville);
		if (ville != null) {
			setNeVille(null);
			setNeEtranger(true);
		}
	}

	// -----

	public String getDesignationLongue() {
		return DESIGNATIONS_LONGUES.get(getDesignation());
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getActivites());
		DeletionUtils.delete(getCharges());
		DeletionUtils.delete(getRessources());

		DeletionUtils.empty(this, DOSSIERS_ACHARGE_PROPERTY,
				DOSSIERS_CHEF_FAMILLE_PROPERTY, DOSSIERS_CONJOINT_PROPERTY,
				DOSSIERS_NON_ACHARGE_PROPERTY);
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, //
				getDossiersChefFamille(), //
				getDossiersConjoint(), //
				getDossiersACharge(), //
				getDossiersNonACharge());
	}

	public int compareTo(Personne o) {
		return new ComparisonChain() //
				.append(getNom(), o.getNom()) //
				.append(getPrenom(), o.getPrenom()) //
				.append(getId(), o.getId()) //
				.result();
	}

	protected Integer getId() {
		return (Integer) DataObjectUtils.pkForObject(this);
	}

}
