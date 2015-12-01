package nc.ccas.gasel.services;

import static nc.ccas.gasel.AppContext.ldap;

public class LdapEnv {

	public static boolean bypassAuth() {
		return ldap("url") == null;
	}

}
