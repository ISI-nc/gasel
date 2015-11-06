package nc.ccas.gasel.stats;

/**
 * <p>
 * Interface pour une transformation. Le but est de contruire la requète
 * permettant d'obtenir les données "brutes" à traiter (ex: nombre d'enfants à
 * charge) pour chaque élément en entrée (ici, des dossiers).
 * </p>
 * 
 * <p>
 * <code>$t</code> est remplacé par la référence à la table source.
 * </p>
 * 
 * <p>
 * Les contraintes du tableau s'appliquent (filtres sur les éléments en entrée).
 * Il n'est donc possible que d'utiliser des "JOIN" pour retrouver les valeurs.
 * </p>
 * 
 * <p>
 * Il est possible d'ajouter des filtres supplémentaires.
 * </p>
 * 
 * <p>
 * La requète finale a pour forme :
 * </p>
 * 
 * <pre>
 * SELECT $t.id, [column definition]
 *   FROM schema.table $t [JOIN table1 t1 ON (clause join 1)]*
 *  WHERE (filtre sur $t) [AND (filtres supplémentaires)]
 * </pre>
 * 
 * @author ISI.NC - Mikaël Cluseau
 * 
 */
public interface Transformation {

	/**
	 * @return The SQL column statement with "$t" for the table alias.
	 */
	public TransformationQuery getQuery(TableauStat tableau);

}
