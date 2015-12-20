package nc.ccas.gasel.model.core.valid;

import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.validator.GaselValidator;


public class UtilisateurValidator extends GaselValidator<Utilisateur> {

	public UtilisateurValidator() {
		super(Utilisateur.class);
	}

	@Override
	public void validateImpl(Utilisateur object) {
		// Pas de doubles (détection par Login)
		if (object.getLogin() != null) {
			error("Login déjà existant.", //
					!checkUnicity(object, //
							"login", object.getLogin()));

		}
	}

}
