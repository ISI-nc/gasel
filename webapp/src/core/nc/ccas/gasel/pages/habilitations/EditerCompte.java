package nc.ccas.gasel.pages.habilitations;

import java.util.UUID;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.core.Utilisateur;

public abstract class EditerCompte extends EditPage<Utilisateur> {

	public EditerCompte() {
		super(Utilisateur.class);
	}
	
	@Override
	protected void prepareNewObject(Utilisateur object) {
		super.prepareNewObject(object);
		object.setPassword(UUID.randomUUID().toString()); // useless but required
	}

	public Utilisateur getCompte() {
		return getObject();
	}
	
	@Override
	public String getTitre() {
		return String.valueOf(getCompte());
	}

}
