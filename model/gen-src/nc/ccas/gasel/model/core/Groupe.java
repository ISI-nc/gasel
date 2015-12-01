package nc.ccas.gasel.model.core;

import static nc.ccas.gasel.model.DeletionUtils.delete;
import static nc.ccas.gasel.modelUtils.CommonQueries.unique;

import java.util.ArrayList;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.core.auto._Groupe;

import org.apache.cayenne.ObjectContext;

public class Groupe extends _Groupe implements ComplexDeletion {
	private static final long serialVersionUID = 5378274274042809697L;

	@Override
	public String toString() {
		return getLibelle();
	}

	public void addToAccesPage(String pageName, boolean read) {
		addToAccesPage(pageName, read, false);
	}

	public void addToAccesPage(String pageName, boolean read, boolean write) {
		ObjectContext context = getObjectContext();

		AppPage page = unique(context, AppPage.class, "page", pageName);
		if (page == null) {
			throw new NullPointerException("No such page: " + pageName);
		}

		AccesPage acces = (AccesPage) context.newObject(AccesPage.class);
		acces.setPage(page);
		acces.setRead(read);
		acces.setWrite(write);
		addToAccesPage(acces);
	}

	public void addToUtilisateurs(String login) {
		Utilisateur utilisateur = unique(getObjectContext(), Utilisateur.class,
				"login", login);
		if (utilisateur == null) {
			throw new NullPointerException("No such user: " + login);
		}
		addToUtilisateurs(utilisateur);
	}

	public void prepareForDeletion() {
		for (AccesPage acces : new ArrayList<AccesPage>(getAccesPage())) {
			removeFromAccesPage(acces);
			delete(acces);
		}
		for (Utilisateur user : new ArrayList<Utilisateur>(getUtilisateurs())) {
			removeFromUtilisateurs(user);
		}
	}

}
