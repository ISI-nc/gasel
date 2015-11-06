package nc.ccas.gasel.services;

public class AuthResult {

	public static AuthResult valid(String nom, String prenom, String email,
			String ref) {
		AuthResult result = new AuthResult();
		result.setNom(nom);
		result.setPrenom(prenom);
		result.setEmail(email);
		result.setRef(ref);
		result.setValid(true);
		return result;
	}

	public static AuthResult invalid() {
		AuthResult result = new AuthResult();
		result.setValid(false);
		return result;
	}

	private String nom;

	private String prenom;

	private String email;

	private String ref;

	private boolean valid;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
