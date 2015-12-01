package nc.ccas.gasel.stats;

import java.util.LinkedList;
import java.util.List;

/**
 * La table source a l'alias <code>t</code>. Les alias de jointure sont
 * fournis en retour de la fonction <code>join</code>.
 * 
 * @author ISI.NC - Mikaël Cluseau
 * 
 */
public class ColumnSelect {

	private int t = 0;

	private String value;

	private final List<String> joins = new LinkedList<String>();

	private final List<String> conditions = new LinkedList<String>();

	public String buildQuery(String tableName) {
		StringBuilder buf = new StringBuilder("SELECT t.id, (");
		buf.append(value).append(")AS v FROM ").append(tableName).append(" t");
		for (String join : joins) {
			buf.append(" JOIN ").append(join);
		}
		if (!conditions.isEmpty()) {
			buf.append(" WHERE (");
			boolean first = true;
			for (String condition : conditions) {
				if (first) {
					first = false;
				} else {
					buf.append(") AND (");
				}
				buf.append(condition);
			}
			buf.append(')');
		}
		buf.append(" ORDER BY t.id");
		return buf.toString();
	}

	private String nextTableAlias() {
		return "t" + t++;
	}

	public String join(String table, String joinClause) {
		String alias = nextTableAlias();
		joins.add(table + " " + alias + " ON (" + joinClause + ")");
		return alias;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getJoins() {
		return joins;
	}

	public List<String> getConditions() {
		return conditions;
	}

}
