package nc.ccas.gasel.stats.tr;

import static nc.ccas.gasel.modelUtils.CayenneUtils.tableFor;
import nc.ccas.gasel.stats.TableauStat;
import nc.ccas.gasel.stats.Transformation;
import nc.ccas.gasel.stats.TransformationQuery;

import org.apache.cayenne.DataObject;

public class EnumerationTr implements Transformation {

	private final String column;

	private String enumerationTable;

	public EnumerationTr(Class<? extends DataObject> enumeration, String column) {
		this.enumerationTable = tableFor(enumeration);
		this.column = column;
	}

	public TransformationQuery getQuery(TableauStat tableau) {
		return new TransformationQuery() //
				.join(enumerationTable, "e", "e.id = $t." + column) //
				.definition("e.libelle");
	}

}
