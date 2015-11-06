package nc.ccas.gasel.services;

public interface AuthLDAP {

	public static final AuthLDAP INSTANCE = new AuthLDAPImpl();

	/**
	 * Auth faible, uniquement par le nom d'utilisateur. Utile pour retrouver
	 * les infos d'un utilisateur.
	 * 
	 * <p>
	 * <strong>Ne pas utiliser pour un login !</strong>
	 * </p>
	 */
	public AuthResult authenticate(String user);

	/**
	 * Auth classique.
	 */
	public AuthResult authenticate(String user, String password);

}
