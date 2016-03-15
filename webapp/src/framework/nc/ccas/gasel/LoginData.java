package nc.ccas.gasel;

import static com.asystan.common.cayenne_new.QueryFactory.createAnd;
import static com.asystan.common.cayenne_new.QueryFactory.createEquals;
import static nc.ccas.gasel.modelUtils.CommonQueries.unique;
import static org.apache.cayenne.access.DataContext.getThreadDataContext;
import static nc.ccas.gasel.modelUtils.CayenneUtils.createDataContext;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nc.ccas.gasel.model.core.AccesPage;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.services.AuthLDAP;
import nc.ccas.gasel.services.AuthResult;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;

import com.asystan.common.cayenne_new.QueryFactory;

public class LoginData implements Serializable {
	private static final long serialVersionUID = -7391145845746492406L;

	public static Utilisateur lookupOrCreateUser(String userName, AuthResult result) {
		DataContext context = createDataContext();
		Utilisateur user = unique(context, Utilisateur.class, //
				createAnd(createEquals("login", userName)));

		if (user == null) {
			synchronized (Utilisateur.class) {
				user = unique(context, Utilisateur.class, //
						createAnd(createEquals("login", userName)));

				if (user == null) {
					user = (Utilisateur) context.newObject(Utilisateur.class);
					user.setActif(true);
					user.setLogin(userName);
					user.setNom(result.getNom());
					user.setPrenom(result.getPrenom());
					user.setPassword(""); // useless
					user.setInitiales((result.getNom().substring(0, 1) //
					+ result.getPrenom().substring(0, 1)).toUpperCase());
					context.commitChanges();
				}
			}
		}
		return user;
	}

	private String userName;

	private String email;

	private HttpServletRequest request;

	public Utilisateur getUser() {
		return getUser(getThreadDataContext());
	}

	private Utilisateur getUser(ObjectContext context) {
		if (!isLoggedIn()) {
			throw new RuntimeException("Not logged in!");
		}
		if (userName == null) {
			login();
		}
		return unique(context, Utilisateur.class, "login", userName);
	}

	public boolean isLoggedIn() {
		return request.getUserPrincipal() != null;
	}

	public boolean login() {
		// We trust the container
		Principal userPrincipal = request.getUserPrincipal();
		if (userPrincipal == null) {
			return false;
		}
		String userName = userPrincipal.getName();
		Utilisateur user = lookupUser(userName);
		if (user == null) {
			return false;
		}
		this.userName = user.getLogin();
		return true;
	}

	private Utilisateur lookupUser(String userName) {
		userName = userName.toLowerCase();

		AuthResult result = AuthLDAP.INSTANCE.authenticate(userName, null);
		if (!result.isValid())
			return null;
		
		email = result.getEmail();

		return lookupOrCreateUser(userName, result);
	}

	public boolean hasAccess(String pageName) {
		if (!restricted(pageName))
			return true; // Page non restreinte

		for (AccesPage acces : accesses(pageName)) {
			if (acces.getRead())
				return true;
		}

		return false;
	}

	public boolean hasWriteAccess(String pageName) {
		if (!restricted(pageName))
			return true; // Page non restreinte

		for (AccesPage acces : accesses(pageName)) {
			if (acces.getWrite())
				return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	private List<AccesPage> accesses(String pageName) {
		DataContext context = CayenneUtils.createDataContext();
		Utilisateur user = getUser(context);

		Expression expr = QueryFactory.createAnd(
				QueryFactory.createEquals("page.page", pageName),
				QueryFactory.createEquals("groupe.utilisateurs", user));

		return context.performQuery(new SelectQuery(AccesPage.class, expr));
	}

	private boolean restricted(String pageName) {
		return Pages.PAGES.containsKey(pageName);
	}

	public String getUserName() {
		return userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
