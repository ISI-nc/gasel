package nc.ccas.gasel.services.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.ccas.gasel.beans.Fusion;
import nc.ccas.gasel.model.aides.AspectAides;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.core.enums.Problematique;
import nc.ccas.gasel.model.habitat.AspectDossierHabitat;
import nc.ccas.gasel.model.habitat.AspectSIE;
import nc.ccas.gasel.model.paph.AspectDossierPAPH;
import nc.ccas.gasel.model.pe.AspectDossierAM;
import nc.ccas.gasel.model.pe.AspectDossierEnfant;
import nc.ccas.gasel.model.pi.AspectDossierPI;
import nc.ccas.gasel.services.Fusionneur;

import org.apache.cayenne.DataObject;

public class FusionneurDossier implements Fusionneur<Dossier> {

	public static final Set<Class<? extends DataObject>> ASPECT_CLASSES = new HashSet<>();
	static {
		ASPECT_CLASSES.add(AspectAides.class);
		ASPECT_CLASSES.add(AspectDossierEnfant.class);
		ASPECT_CLASSES.add(AspectDossierAM.class);
		ASPECT_CLASSES.add(AspectDossierHabitat.class);
		ASPECT_CLASSES.add(AspectDossierPAPH.class);
		ASPECT_CLASSES.add(AspectDossierPI.class);
		ASPECT_CLASSES.add(AspectSIE.class);
	}

	// FIXME should DI'ed
	private static final Map<Class<?>, Fusionneur<?>> FUSIONNEURS = new HashMap<>();
	static {
		FUSIONNEURS.put(AspectAides.class, new FusionneurAspectAides());
	}

	private Map<Class<?>, Fusionneur<?>> fusionneurs;

	// FIXME breaks DI
	public FusionneurDossier() {
		this(FUSIONNEURS);
	}

	public FusionneurDossier(Map<Class<?>, Fusionneur<?>> fusionneurs) {
		this.fusionneurs = fusionneurs;
	}

	@Override
	public void fusionner(Fusion<Dossier> fusion) {
		Dossier main = fusion.getElementPrincipal();
		Dossier sec = fusion.getElementSecondaire();

		for (Personne personne : sec.getPersonnes()) {
			main.addToPersonnesNonACharge(personne);
		}

		for (Problematique problematique : sec.getProblematiques()) {
			main.addToProblematiques(problematique);
		}

		for (Class<? extends DataObject> aspectClass : ASPECT_CLASSES) {
			fusionneClasses(fusion, aspectClass);
		}

		sec.getObjectContext().deleteObject(sec);
	}

	private <T extends DataObject> void fusionneClasses(Fusion<Dossier> fusion,
			Class<T> aspectClass) {
		T main = fusion.getElementPrincipal().getAspect(aspectClass);
		T sec = fusion.getElementSecondaire().getAspect(aspectClass);

		if (sec == null) {
			// Rien à fusionner
			return;
		}

		if (main == null && sec != null) {
			// Transférer sur le principal
			sec.setToOneTarget("dossier", main, true);
			return;
		}

		@SuppressWarnings("unchecked")
		Fusionneur<T> fusionneur = (Fusionneur<T>) fusionneurs.get(aspectClass);
		if (fusionneur == null) {
			throw new RuntimeException("Pas de fusionneurs pour la classe "
					+ aspectClass.getCanonicalName());
		}

		Fusion<T> fusionAspects = new Fusion<T>();
		fusionAspects.setElementPrincipal(main);
		fusionAspects.setElementSecondaire(sec);

		fusionneur.fusionner(fusionAspects);
	}
}
