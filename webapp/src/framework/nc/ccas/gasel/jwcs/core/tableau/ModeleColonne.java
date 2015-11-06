package nc.ccas.gasel.jwcs.core.tableau;

import java.util.List;

import org.apache.tapestry.annotations.Parameter;

public interface ModeleColonne {

	/**
	 * Les titres des colonnes.
	 */
	public List<String> getTitres();

	/**
	 * Les valeurs.
	 */
	public List<Cellule> getValeurs();

	/**
	 * Indique si le composant est visible.
	 */
	@Parameter(defaultValue = "ognl:true")
	public boolean getVisible();

}
