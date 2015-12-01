package nc.ccas.gasel.model.core.stats;

import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.stats.tr.SqlTr;

public class TypeSuiviTr extends SqlTr {

	public static final TypeSuiviTr INSTANCE = new TypeSuiviTr();

	private TypeSuiviTr() {
		super(Dossier.aspectsActifsSql("$t.id"));
	}

}
