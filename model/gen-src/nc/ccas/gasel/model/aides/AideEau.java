package nc.ccas.gasel.model.aides;

import java.util.List;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.aides.auto._AideEau;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.DataObjectUtils;

@Feminin
public class AideEau extends _AideEau {
	private static final long serialVersionUID = 8377935690714856277L;

	public Aide getAide() {
		List<Aide> aides = CommonQueries.select(getObjectContext(), Aide.class,
				"eau = " + DataObjectUtils.intPKForObject(this));
		return aides.isEmpty() ? null : aides.get(0);
	}

}
