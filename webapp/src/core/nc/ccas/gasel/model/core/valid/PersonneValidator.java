package nc.ccas.gasel.model.core.valid;

import nc.ccas.gasel.Utils;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.validator.GaselValidator;

public class PersonneValidator extends GaselValidator<Personne> {

	public PersonneValidator() {
		super(Personne.class);
	}

	@Override
	public void validateImpl(Personne object) {

		// Contrôle des noms
		if (object.getNom() == null && object.getNomJeuneFille() == null) {
			error("Pas nom ou de nom de jeune fille.");
		}

		if (object.getSexe() != null && object.isHomme()
				&& object.getNomJeuneFille() != null) {
			error("Nom de jeune fille pour un homme.");
		}

		// Validation générique
		validateDataObject(object);

		// Date de naissance
		if (object.getDateNaissance() != null
				&& object.getDateNaissance().after(DateUtils.today())) {
			error("Date de naissance dans le futur.");
		}

		// Cohérence sexe/désignation
		if (object.getSexe() != null && object.getDesignation() != null) {
			boolean designationM = "M".equals(object.getDesignation());
			if (object.isHomme()) {
				error("Désignation invalide pour un homme ("
						+ object.getDesignation() + ")", !designationM);
			} else {
				error("Désignation invalide pour une femme ("
						+ object.getDesignation() + ")", designationM);
			}
		}

		// Pas de doubles (détection par nom, prénom et date de naissance)
		if (object.getPrenom() != null && object.getDateNaissance() != null) {
			if (object.getNom() != null) {
				error("Personne déjà existante.", //
						!checkUnicity(object, //
								"nom", object.getNom(), //
								"prenom", object.getPrenom(), //
								"dateNaissance", object.getDateNaissance()));
			}
			if (object.getNomJeuneFille() != null) {
				error("Personne déjà existante.", //
						!checkUnicity(object, //
								"nomJeuneFille", object.getNomJeuneFille(), //
								"prenom", object.getPrenom(), //
								"dateNaissance", object.getDateNaissance()));
			}
		}

		// Date entrée dans le foyer
		error("L'entrée au foyer doit être dans le passé", //
				!dates.checkPassee(object.getDateEntreeFoyer()));
		error("Entrée au foyer est avant la naissance", //
				!dates.checkOrder(object.getDateNaissance(), object
						.getDateEntreeFoyer()));
		error("Sortie du foyer avant l'entrée", //
				!dates.checkOrder(object.getDateEntreeFoyer(), object
						.getDateSortieFoyer()));
		// Date arrivée sur le territoire
		error("Arrivée sur le territoire avant la naissance", //
				!dates.checkOrder(object.getDateNaissance(), object
						.getDateArriveeSurTerritoire()));
		// Date de décès
		error("Décès avant la naissance", //
				!dates.checkOrder(object.getDateNaissance(), object
						.getDateDeces()));

		// --------------------------------------------------------------------
		// Alertes
		//

		// Mutuelle
		if (requireProperty(false, object, "mutuelle")) {
			requireProperty(false, object, "numeroMutuelle");
			if (requireProperty(false, object, "finMutuelle")) {
				warning("Mutuelle expirée", //
						dates.checkPassee(object.getFinMutuelle()));
			}
		}

		// Couverture sociale
		if (requireProperty(false, object, "couvertureSociale")) {
			requireProperty(false, object, "numeroCouvSoc");
			if (requireProperty(false, object, "finCouvSoc")) {
				if (Utils.today().after(object.getFinCouvSoc())) {
					warning("Couverture sociale expirée");
				}
			}
		}
	}

}
