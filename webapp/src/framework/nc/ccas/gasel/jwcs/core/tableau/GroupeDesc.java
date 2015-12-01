package nc.ccas.gasel.jwcs.core.tableau;

public class GroupeDesc {

	private final int largeur;

	private final String titre;

	public GroupeDesc(int largeur, String titre) {
		this.largeur = largeur;
		this.titre = titre;
	}

	public int getLargeur() {
		return largeur;
	}

	public String getTitre() {
		return titre;
	}

}
