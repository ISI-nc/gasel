package gasel.maintenance;

import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.access.DataContext;


public class CreateUser {
	
	public static void main(String[] args) {
		if (true) { // secu
			System.exit(0);
		}
		String login = "beumi76";
		String nom =  "MAILLE";
		String prenom = "Michelle";
		
		
		System.out.println("User login: " + login);
		
		DataContext context = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(context);
		
		Utilisateur newUser = (Utilisateur) context.newObject(Utilisateur.class);
		newUser.setActif(true);
		newUser.setInitiales(nom.substring(0,1).toUpperCase()+prenom.substring(0,1).toUpperCase());
		newUser.setLogin(login);
		newUser.setNom(nom);
		newUser.setPrenom(prenom);
		newUser.setPassword("");
		
		context.commitChanges();
		System.out.println("User créé");
		
		context.deleteObject(newUser);
		context.commitChanges();
		System.out.println("User supprimé");
	}

}
