package nc.ccas.gasel.pages;

import nc.ccas.gasel.BasePage;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;

/**
 * 
 * @author nwrk
 * 
 * @deprecated Useless since we use container managed autentication.
 *
 */
@Deprecated
public abstract class Home extends BasePage {

	public abstract String getUserId();

	public abstract String getPassword();
	
	@Persist("flash")
	public abstract String getError();

	public abstract void setError(String error);

	public void doLogin() {
		IRequestCycle cycle = getRequestCycle();
		getLogin().login(); // XXX deprecated
		if (!getLogin().isLoggedIn()) {
			log("Login FAILED: " + getUserId());
			setError("Nom d'utilisateur et/ou mot de passe incorrect.");
			return;
		}
		if (!getLogin().getUser().getActif()) {
			log("Login FAILED: " + getUserId() + " [inactif]");
			setError("Compte désactivé.");
			return;
		}
		log("Login OK: " + getUserId());
		getWorkflow().redirect(cycle);
	}

	@Override
	public void validate(IRequestCycle cycle) {
		super.validate(cycle);
		if (getLogin().isLoggedIn()) {
			getWorkflow().redirect(cycle);
			return;
		}
	}

}
