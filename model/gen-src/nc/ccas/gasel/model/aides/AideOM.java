package nc.ccas.gasel.model.aides;

import java.util.List;

import org.apache.cayenne.DataObjectUtils;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.aides.auto._AideOM;
import nc.ccas.gasel.modelUtils.CommonQueries;

@Feminin
public class AideOM extends _AideOM {
	private static final long serialVersionUID = 9032475081393636410L;

	public Aide getAide() {
		List<Aide> aides = CommonQueries.select(getObjectContext(), Aide.class,
				"orduresMenageres = " + DataObjectUtils.intPKForObject(this));
		return aides.isEmpty() ? null : aides.get(0);
	}

}
