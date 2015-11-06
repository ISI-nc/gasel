package nc.ccas.gasel.stats;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.map.DbEntity;

public class TransformationQuery {

	private String columnDefinition;

	private final List<TransformationQueryJoinDef> joins = new LinkedList<TransformationQueryJoinDef>();

	private final Set<String> usedAliases = new HashSet<String>();

	private List<String> qualifiers = new LinkedList<String>();

	private boolean aggregate = false;

	/**
	 * @return La requête SQL de colonne statistique.
	 */
	public String sql(String sourceTable, String sourceTableAlias,
			String sourceFilter) {
		checkAlias(sourceTableAlias);
		StringBuilder buf = new StringBuilder();

		// Colonnes
		buf.append("SELECT DISTINCT $t.id, (" + columnDefinition + ") AS v\n");

		// From et jointures
		buf.append("  FROM " + sourceTable + " $t\n");
		for (TransformationQueryJoinDef join : joins)
			buf.append("  JOIN " + join.sql() + "\n");

		// Where
		StringBuilder whereClause = new StringBuilder();
		if (sourceFilter != null || !qualifiers.isEmpty()) {
			buf.append(" WHERE ");
		}
		if (sourceFilter != null) {
			whereClause.append("(").append(sourceFilter).append(")\n");
		}
		for (String qualifier : qualifiers) {
			if (whereClause.length() > 0)
				whereClause.append("   AND ");
			whereClause.append("(").append(qualifier.trim()).append(")\n");
		}
		buf.append(whereClause);

		// Order
		if (aggregate) {
			buf.append("GROUP BY $t.id");
		}
		buf.append(" ORDER BY $t.id");

		return buf.toString().replaceAll("\\B\\$t\\b", sourceTableAlias);
	}

	/**
	 * @return La requête SQL de colonne statistique.
	 */
	public String sql(Class<? extends DataObject> sourceClass,
			String sourceTableAlias, String sourceFilter) {
		return sql(classToTable(sourceClass), sourceTableAlias, sourceFilter);
	}

	public TransformationQuery definition(String columnDefinition) {
		this.columnDefinition = columnDefinition;
		return this;
	}

	/**
	 * Ajoute une jointure.
	 * 
	 * @param table
	 *            Le nom de la table jointe (avec schéma).
	 * 
	 * @param alias
	 *            L'alias demandé.
	 * 
	 * @param onClause
	 *            Clause de jointure ("$t" est remplacé par l'alias de la table
	 *            source).
	 * 
	 * @return this.
	 */
	public TransformationQuery join(String table, String alias, String onClause) {
		registerAlias(alias);
		joins.add(new TransformationQueryJoinDef(table, alias, onClause));
		return this;
	}

	/**
	 * Ajoute une jointure.
	 * 
	 * @param target
	 *            La DbEntity jointe, traduite en table.
	 * 
	 * @param alias
	 *            L'alias demandé.
	 * 
	 * @param onClause
	 *            Clause de jointure ("$t" est remplacé par l'alias de la table
	 *            source).
	 * 
	 * @return this.
	 */
	public TransformationQuery join(DbEntity target, String alias,
			String onClause) {
		return join(target.getFullyQualifiedName(), alias, onClause);
	}

	/**
	 * Ajoute une jointure.
	 * 
	 * @param clazz
	 *            La classe d'objet jointe, traduite en table.
	 * 
	 * @param alias
	 *            L'alias demandé.
	 * 
	 * @param onClause
	 *            Clause de jointure ("$t" est remplacé par l'alias de la table
	 *            source).
	 * 
	 * @return this.
	 */
	public TransformationQuery join(Class<? extends DataObject> clazz,
			String tableAlias, String onClause) {
		return join(classToTable(clazz), tableAlias, onClause);
	}

	/**
	 * Traduit une classe en nom de table.
	 * 
	 * @throws IllegalArgumentException
	 *             S'il n'y a pas de table pour classe.
	 */
	private String classToTable(Class<? extends DataObject> clazz) {
		DbEntity dbEnt = CayenneUtils.entityResolver().lookupObjEntity(clazz).getDbEntity();
		if (dbEnt == null) {
			throw new IllegalArgumentException("Pas de table pour la classe "
					+ clazz);
		}
		return dbEnt.getFullyQualifiedName();
	}

	/**
	 * Enregistre un alias.
	 * 
	 * @throws IllegalArgumentException
	 *             Si l'alias a déjà été utilisé.
	 */
	private synchronized void registerAlias(String alias) {
		checkAlias(alias);
		usedAliases.add(alias);
	}

	/**
	 * Vérifie qu'un alias est disponible.
	 * 
	 * @throws IllegalArgumentException
	 *             Si l'alias a déjà été utilisé.
	 */
	private void checkAlias(String alias) {
		if (usedAliases.contains(alias)) {
			throw new IllegalArgumentException("Alias déjà utilisé : " + alias);
		}
	}

	public TransformationQuery addQualifier(String qualifier) {
		qualifiers.add(qualifier);
		return this;
	}

	public TransformationQuery aggregate() {
		return aggregate(true);
	}

	public TransformationQuery aggregate(boolean value) {
		aggregate = value;
		return this;
	}
	
	// --------------- getters

	public String getColumnDefinition() {
		return columnDefinition;
	}

	public List<TransformationQueryJoinDef> getJoins() {
		return joins;
	}

	public Set<String> getUsedAliases() {
		return usedAliases;
	}

	public List<String> getQualifiers() {
		return qualifiers;
	}

	public boolean isAggregate() {
		return aggregate;
	}

}
