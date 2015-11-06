package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.core.enums.auto._OrigineSignalement;
import nc.ccas.gasel.modelUtils.EnumerationSync;

import org.apache.cayenne.DataObjectUtils;

@Feminin
public class OrigineSignalement extends _OrigineSignalement {
	private static final long serialVersionUID = 397847309981937932L;

	public static final int SPONTANE = 1;

	static {
		EnumerationSync sync = new EnumerationSync(OrigineSignalement.class);
		sync.add(SPONTANE, "Spontané");
	}

	public Integer getId() {
		if (getObjectId().isTemporary())
			return null;
		return DataObjectUtils.intPKForObject(this);
	}

	public boolean isSpontane() {
		return getId() == SPONTANE;
	}

}
