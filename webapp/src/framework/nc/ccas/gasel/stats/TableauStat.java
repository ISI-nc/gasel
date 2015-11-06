package nc.ccas.gasel.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.ResultIterator;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.query.SQLTemplate;

/**
 * <p>
 * Un tableau statistique est composé de colonnes. Celles-ci sont composées à
 * partir de données extraites grace à des <code>Transformation</code>s et
 * représentées avec des <code>Representation</code>s.
 * </p>
 * 
 * @author ISI.NC - Mikaël Cluseau
 * 
 */
public class TableauStat {
	private final String source;

	private final List<String> selector = new LinkedList<String>();

	private final List<ColumnDefinition> colonnes;

	private ColNode root;

	private boolean full;

	private HashMap<Integer, DataItem> data;

	public TableauStat(Class<? extends Persistent> source,
			List<ColumnDefinition> colonnes) {
		this(source.getSimpleName(), colonnes);
	}

	public TableauStat(Class<? extends Persistent> source,
			ColumnDefinition... colonnes) {
		this(source.getSimpleName(), colonnes);
	}

	public TableauStat(String source, List<ColumnDefinition> colonnes) {
		this.source = source;
		this.colonnes = colonnes;
		clear();
	}

	public TableauStat(String source, ColumnDefinition... colonnes) {
		this(source, Arrays.asList(colonnes));
	}

	@SuppressWarnings("unchecked")
	public void fill() {
		if (full)
			return;

		int numCols = colonnes.size();

		data = new HashMap<Integer, DataItem>();

		String qualifier = null;
		if (!selector.isEmpty()) {
			StringBuilder buf = new StringBuilder();
			for (String expr : selector) {
				if (buf.length() > 0)
					buf.append(" AND ");
				buf.append('(').append(expr).append(')');
			}
			qualifier = buf.toString().replace("$t", "t");
		}

		// Remplissage des données (colonne par colonne)
		ObjEntity entity = entity();
		Class<? extends DataObject> sourceClass = entity.getJavaClass()
				.asSubclass(DataObject.class);
		DataContext dc = CayenneUtils.createDataContext();
		for (int i = 0; i < numCols; i++) {
			SQLTemplate tmpl = new SQLTemplate(entity, colonnes.get(i).getTr()
					.getQuery(this).sql(sourceClass, "t", qualifier));
			ResultIterator results;

			try {
				results = dc.performIteratedQuery(tmpl);
			} catch (Exception e) {
				throw new RuntimeException(//
						String.format("Col %d, query: %s", //
								i + 1, tmpl.getDefaultTemplate()), //
						e);
			}

			try {
				while (results.hasNextRow()) {
					Map<String, Object> row = (Map<String, Object>) results.nextDataRow();
					Integer id = (Integer) row.get("id");
					Object value = row.get("v");
					DataItem item = data.get(id);
					if (item == null) {
						item = new DataItem(numCols);
						data.put(id, item);
					}
					item.add(i, value);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		for (DataItem item : data.values()) {
			inject(item);
		}
		full = true;
	}

	public List<List<FactCell>> expand() {
		List<List<FactCell>> retval = new ArrayList<List<FactCell>>();
		for (FactLine line : root.getLines()) {
			retval.addAll(line.expand());
		}
		return retval;
	}

	public void clear() {
		root = new ColNode(0, colonnes);
		full = false;
		data = null;
	}

	private void inject(DataItem item) {
		ColNode node = root;
		while (node != null) {
			node = node.process(item);
		}
	}

	public ObjEntity entity() {
		return CayenneUtils.entityResolver().getObjEntity(source);
	}

	public List<ColumnDefinition> getColonnes() {
		return colonnes;
	}

	public String toPlainText() {
		List<List<FactCell>> expand = expand();

		// Calcul des largeurs de colonne
		StringBuilder buf = new StringBuilder();
		int[] largeurs = new int[getColonnes().size()];
		for (int i = 0; i < largeurs.length; i++) {
			int max = 1;
			for (List<FactCell> row : expand) {
				FactCell cell = row.get(i);
				if (cell == null)
					continue;
				String repr = cell.toString();
				if (repr.length() > max)
					max = repr.length();
			}
			if (buf.length() > 0) {
				buf.append(" | ");
			}
			buf.append("%-" + max + "s");
			largeurs[i] = max;
		}
		buf.append('\n');
		String pattern = buf.toString();

		// Sortie
		StringBuilder out = new StringBuilder();
		for (List<FactCell> row : expand) {
			// Bordure
			boolean needBorder = false;
			Object[] borderTop = new Object[row.size()];
			for (int i = 0; i < borderTop.length; i++) {
				FactCell cell = row.get(i);
				char c;
				if (cell == null || cell.isDottedBorder()) {
					c = ' ';
				} else {
					c = '-';
					needBorder = true;
				}

				StringBuilder buf2 = new StringBuilder();
				for (int j = 0; j < largeurs[i]; j++)
					buf2.append(c);
				borderTop[i] = buf2.toString();
			}
			if (needBorder)
				out.append(String.format(pattern, borderTop));

			Object[] rowRepr = new Object[row.size()];
			for (int i = 0; i < rowRepr.length; i++) {
				rowRepr[i] = row.get(i) == null ? "" : row.get(i).toString();
			}
			out.append(String.format(pattern, rowRepr));
		}
		return out.toString();
	}

	public List<String> getSelector() {
		return selector;
	}

	public void addToSelector(String critere) {
		selector.add(critere);
	}

}
