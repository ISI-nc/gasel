package nc.ccas.gasel.stats.tr;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nc.ccas.gasel.stats.TableauStat;
import nc.ccas.gasel.stats.Transformation;
import nc.ccas.gasel.stats.TransformationQuery;

import org.apache.cayenne.map.DbEntity;
import org.apache.cayenne.map.DbJoin;
import org.apache.cayenne.map.DbRelationship;
import org.apache.cayenne.map.ObjAttribute;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;

public class ObjPathTr implements Transformation {

	public static final ObjPathTr count(String path) {
		return new ObjPathTr(path, "COUNT(*)").aggregate();
	}

	private final String path;

	private final String select;

	private final List<String> qualifiers = new LinkedList<String>();

	private boolean aggregate = false;

	/**
	 * @param path
	 *            Le chemin jusqu'à la cible. Doit arriver sur un attribut (ex:
	 *            <code>dossier.chefFamille.nom</code>).
	 */
	public ObjPathTr(String path) {
		this(path, null);
	}

	/**
	 * Exemples :
	 * 
	 * <pre>
	 * // Nom du chef de famille
	 * new ObjPathTr(&quot;dossier.chefFamille.nom&quot;);
	 * // Nom et prénom du chef de famille
	 * new ObjPathTr(&quot;dossier.chefFamille&quot;, &quot;$t.nom||', '||$t.prenom&quot;);
	 * // Nombre de personnes à charge
	 * new ObjPathTr(&quot;dossier.personnesACharge&quot;, &quot;COUNT(*)&quot;);
	 * </pre>
	 * 
	 * @param path
	 *            Le chemin jusqu'à la cible.
	 * 
	 * @param select
	 *            La sélection finale, où <code>$t</code> sera remplacé par la
	 *            dernière table du <code>path</code>. Si ce paramètre est
	 *            <code>null</code>, <code>path</code> doit pointer sur un
	 *            attribut. Sinon, il doit pointer sur une entité.
	 */
	public ObjPathTr(String path, String select) {
		this.path = path;
		this.select = select;
	}

	public ObjPathTr aggregate() {
		aggregate = true;
		return this;
	}

	public TransformationQuery getQuery(TableauStat tableau) {
		TransformationQuery query = new TransformationQuery();
		if (aggregate)
			query.aggregate();

		String[] path = this.path.split("\\.");

		Map<String, String> pathToAlias = new TreeMap<String, String>();
		StringBuilder currentPath = new StringBuilder();

		ObjEntity joinSrc = tableau.entity();
		String joinSrcAlias = "$t";

		boolean hasSelectClause = (select != null);
		int alias = 0;

		// Jointures
		int max = (hasSelectClause ? path.length : path.length - 1);
		for (int j = 0; j < max; j++) {
			if (currentPath.length() > 0)
				currentPath.append('.');
			currentPath.append(path[j]);

			ObjRelationship joinObjRel = (ObjRelationship) joinSrc
					.getRelationship(path[j]);
			if (joinObjRel == null) {
				throw new RuntimeException(String.format(
						"No relationship named \"%s\" in entity %s (at %s)",
						path[j], joinSrc.getName(), currentPath.toString()));
			}
			ObjEntity joinDst = (ObjEntity) joinObjRel.getTargetEntity();
			String joinDstAlias = null;

			// Au moins une itération
			for (Object o : joinObjRel.getDbRelationships()) {
				DbRelationship dbRel = (DbRelationship) o;
				DbEntity target = (DbEntity) dbRel.getTargetEntity();
				joinDstAlias = "t" + alias++;

				query.join(target, joinDstAlias, //
						joinOnClause(joinSrcAlias, joinDstAlias, dbRel));

				// Au suivant (join)
				joinSrcAlias = joinDstAlias;
			}
			// Au suivant (path element)
			joinSrc = joinDst;
			pathToAlias.put("${" + currentPath + "}", joinSrcAlias);
		}

		// Conditions
		for (String qualifier : qualifiers) {
			for (Map.Entry<String, String> tr : pathToAlias.entrySet()) {
				qualifier = qualifier.replace(tr.getKey(), tr.getValue());
			}
			query.addQualifier(qualifier);
		}

		// Définition de la colonne de résultat
		String selectField;
		if (hasSelectClause) {
			selectField = select.replace("$t", joinSrcAlias);
		} else {
			// Pas de clause pour la colonne du "select", on utilise le dernier
			// élément du chemin
			String attributeName = ((ObjAttribute) joinSrc
					.getAttribute(path[path.length - 1])).getDbAttributeName();
			selectField = joinSrcAlias + "." + attributeName;
		}
		query.definition(selectField);

		// end
		return query;
	}

	private String joinOnClause(String joinSrcAlias, String joinDstAlias,
			DbRelationship dbRel) {
		StringBuilder buf = new StringBuilder();
		boolean firstJoin = false;
		for (Object o1 : dbRel.getJoins()) {
			DbJoin join = (DbJoin) o1;
			join.getSourceName();
			if (firstJoin) {
				firstJoin = false;
				buf.append(" AND ");
			}
			buf.append(joinSrcAlias + "." + join.getSourceName());
			buf.append("=");
			buf.append(joinDstAlias + "." + join.getTargetName());
		}
		return buf.toString();
	}

	public void addQualifier(String qualifier) {
		qualifiers.add(qualifier);
	}
}
