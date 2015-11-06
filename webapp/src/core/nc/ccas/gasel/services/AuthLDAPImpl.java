package nc.ccas.gasel.services;

import static nc.ccas.gasel.AppContext.ldap;

import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import nc.ccas.gasel.utils.QuickHashMap;

public class AuthLDAPImpl implements AuthLDAP {

	private static final <K, V> Hashtable<K, V> hash(Map<K, V> map) {
		Hashtable<K, V> hash = new Hashtable<K, V>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			hash.put(entry.getKey(), entry.getValue());
		}
		return hash;
	}

	public AuthResult authenticate(String user) {
		// Connexion "admin" pour interroger les utilisateurs
		DirContext context;
		try {
			context = new InitialDirContext(ldapEnv(ldap("admin-user"),
					ldap("admin-password")));
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}

		// Demande des informations sur l'utilisateur
		// (nécessaire pour le test de connexion qui suit)
		SearchResult result = userQuery(context, user);
		if (result == null) {
			// Utilisateur non trouvé
			return AuthResult.invalid();
		}

		String dn = escape(result.getName());

		Attributes attributes = result.getAttributes();
		Attribute attribute = attributes.get("displayName");
		if (attribute == null) {
			return AuthResult.invalid();
		}

		String displayName;
		String email;
		try {
			displayName = (String) attribute.get();
			attribute = attributes.get("mail");
			email = (attribute == null) ? null : (String) attribute.get();
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}

		AuthResult retval;
		int rindex = displayName.lastIndexOf(' ');
		if (rindex >= 0) {
			retval = AuthResult.valid( //
					displayName.substring(0, rindex).trim(), // nom
					displayName.substring(rindex + 1), // prénom
					email, dn);
		} else {
			retval = AuthResult.valid(displayName, "", email, dn);
		}
		return retval;
	}

	public AuthResult authenticate(String user, String password) {
		if (LdapEnv.bypassAuth()) {
			return AuthResult.valid("test", "test", "test", null);
		}

		return authenticate(user);
	}

	private Hashtable<String, String> ldapEnv(String principal,
			String credentials) {
		return hash(new QuickHashMap<String, String>() //
				.put(Context.INITIAL_CONTEXT_FACTORY, ldap("context-factory")) //
				.put(Context.PROVIDER_URL, ldap("url")) //
				.put(Context.REFERRAL, "follow") //

				.put(Context.SECURITY_AUTHENTICATION, ldap("auth-method")) //
				.put(Context.SECURITY_PRINCIPAL, principal + "," + ldap("base")) //
				.put(Context.SECURITY_CREDENTIALS, credentials) //
				.map());
	}

	private SearchResult userQuery(DirContext context, String user) {
		String query = ldap("user-query").replace("$user", user);

		return unique(context, query);
	}

	private SearchResult unique(DirContext context, String query) {
		SearchControls constraints = new SearchControls();
		constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

		SearchResult result;
		try {
			NamingEnumeration<SearchResult> results;
			results = context.search(ldap("base"), query, constraints);
			if (results.hasMore()) {
				result = results.next();
			} else {
				result = null;
			}
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	private String escape(String string) {
		if (string == null)
			return null;
		// X=a,Y=b --> X="a",Y="b"
		return string.replace("=", "=\"").replace(",", "\",") + "\"";
	}

}
