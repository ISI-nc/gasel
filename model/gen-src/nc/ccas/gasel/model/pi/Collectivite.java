package nc.ccas.gasel.model.pi;

import static com.asystan.common.cayenne_new.QueryFactory.createEquals;
import static nc.ccas.gasel.modelUtils.CommonQueries.select;
import static org.apache.cayenne.DataObjectUtils.intPKForObject;

import java.util.Collections;
import java.util.List;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.pi.auto._Collectivite;

@Feminin
public class Collectivite extends _Collectivite implements ComplexDeletion {

	private static final long serialVersionUID = 9117531604164319616L;

	@Override
	public String toString() {
		return getDesignation();
	}

	public List<AttributionJF> getAttributions() {
		if (getObjectId().isTemporary())
			return Collections.emptyList();

		return select(AttributionJF.class, //
				createEquals("demande.collectivite", intPKForObject(this)));
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getDemandes());
	}

}
