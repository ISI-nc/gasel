package nc.ccas.gasel.jwcs.core.edit;

import java.util.Collection;
import java.util.List;

import nc.ccas.gasel.jwcs.core.edit.subset.SubSetListHandler;

import org.apache.cayenne.DataObject;
import org.apache.tapestry.annotations.Parameter;

public interface SubSetParameters<T> {

	// ------------------------------------------------------------------------

	/**
	 * Les éléments dans lesquels on peut choisir.
	 */
	@Parameter(required = true)
	public abstract List<T> getElements();

	/**
	 * Texte du label de la case à cocher pour <code>value</code>.
	 */
	@Parameter(defaultValue = "ognl:value", cache = false)
	public abstract Object getLabel();

	/**
	 * Valeur en cours pendant l'itération sur <code>elements</code>.
	 */
	@Parameter(name = "value", cache = false)
	public abstract T getValueParameter();

	public abstract void setValueParameter(Object object);

	// ------------------------------------------------------------------------

	/**
	 * Handler pour les valeurs à cocher.
	 * 
	 * Exclusif avec <code>values</code> et <code>from</code>.
	 */
	@Parameter
	public abstract SubSetListHandler<T> getHandler();

	// ------------------------------------------------------------------------

	/**
	 * Liste des valeurs cochées.
	 * 
	 * Exclusif avec <code>handler</code> et <code>from</code>.
	 */
	@Parameter(name = "values")
	public abstract Collection<T> getValuesParam();

	// ------------------------------------------------------------------------

	/**
	 * Objet source pour les méthodes d'obtention d'un handler depuis un
	 * DataObject.
	 * 
	 * Exclusif avec <code>handler</code> et <code>from</code>.
	 */
	@Parameter(name = "from")
	public abstract DataObject getSourceObject();

	/**
	 * Nom de la liste des valeurs à cocher dans l'objet.
	 * 
	 * Implique <code>from</code>.
	 * 
	 * Exclusif avec <code>path</code>.
	 */
	@Parameter
	public abstract String getList();

	/**
	 * Chemin vers les valeurs à cocher depuis la source dans le format
	 * <code>toManyName.toOneName</code>.
	 * 
	 * Implique <code>from</code>.
	 * 
	 * Exclusif avec <code>list</code>.
	 */
	@Parameter(name = "path")
	public abstract String getPathFromSource();

	// ------------------------------------------------------------------------

	/**
	 * nombre de colonnes pour le tableau des valeurs à cocher.
	 */
	@Parameter(defaultValue = "3", name = "cols")
	public abstract Integer getColumnCount();

}
