package nc.ccas.gasel.model.habitat.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.habitat.enums.auto._OrigineSignalementHb;

@Feminin
public class OrigineSignalementHb extends _OrigineSignalementHb {
	private static final long serialVersionUID = -9143816854253183335L;

	@Override
	public String toString() {
		return getLibelle();
	}

}



