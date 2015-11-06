package nc.ccas.gasel.model.aides.valid;

import static nc.ccas.gasel.modelUtils.CommonQueries.findById;
import static nc.ccas.gasel.modelUtils.DateUtils.apresFinMois;
import static nc.ccas.gasel.modelUtils.DateUtils.finMois;
import static nc.ccas.gasel.modelUtils.DateUtils.today;
import static org.apache.cayenne.DataObjectUtils.intPKForObject;

import java.util.Date;

import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.validator.GaselValidator;

import org.apache.cayenne.access.DataContext;

public class AideValidator extends GaselValidator<Aide> {

	public AideValidator() {
		super(Aide.class);
	}

	@Override
	protected void validateImpl(Aide aide) {
		validateDataObject(aide);
		if (aide.getNature() != null) {
			if (aide.getNature().isEau()) {
				subValidate("Eau", aide.getEau());
			}
			if (aide.getNature().isOrduresMenageres()) {
				subValidate("Ordures ménagères", aide.getOrduresMenageres());
			}
		}

		// Vérification de la période en fonction du statut
		if (!isInDb(aide) && aide.getStatut() != null //
				&& aide.getDebut() != null && aide.getFin() != null) {
			StatutAide f = aide.getStatut();
			Date debut = aide.getDebut();
			Date fin = aide.getFin();
			if (f.isImmediate()) {
				// début entre mainteant et la fin du mois
				if (debut.before(today()) || debut.after(finMois())) {
					error("Aide immédiate : doit commencer entre aujourd'hui et la fin du mois.");
				}
				// fin avant la fin du mois
				if (fin.after(finMois())) {
					error("Aide immédiate : doit finir dans le mois.");
				}
			}
			if (f.isOccasionnelle() || f.isPlurimensuelle()) {
				if (!apresFinMois(debut)) {
					error("Aide occasionnelle ou plurimensuel : ne peut commencer dans le mois.");
				}
			}
		}

		Aide inDb = null;
		if (isInDb(aide)) {
			inDb = findById(Aide.class, aide.getId(),
					DataContext.createDataContext());
			if (aide.getHasBonsEdites()) {
				if (!inDb.getDebut().equals(aide.getDebut())) {
					// Date début modifiée
					error("La date de début ne peut plus être modifiée.");
				}
			}
			if (!inDb.getFin().equals(aide.getFin())) {
				// Date fin modifiée
				if (aide.getFin().before(today())) {
					error("La date de fin ne peut plus être inférieure à aujourd'hui.");
				}
			}
		}

		// vérification qu'il n'existe pas déjà une aide de même Imputation,
		// Secteur et Article pour un même mois et un mm dossier
		// Modification pour PB Recette 6: Ajout d'une aide identique à une aide
		// dont tous les bons ont été annulés.
		// Romain: 21/10/2005: Je vérifie en plus, pour les aides URGENTES (pas
		// pour les aides O et P car conflits possibles pour les mois à venir),
		// que tous les bons n'ont pas été annulés.
		if (!isInDb(aide) && aide.getNature() != null) {
			for (Aide aide2 : aide.getDossier().getAides()) {
				if (aide == aide2)
					continue;
				if (!aide.enConflit(aide2))
					continue;
				warning("Conflit avec l'aide n°" + intPKForObject(aide2));
				break;
			}
		}
	}

}
