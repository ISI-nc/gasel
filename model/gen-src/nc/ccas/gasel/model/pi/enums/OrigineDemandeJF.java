package nc.ccas.gasel.model.pi.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.pi.enums.auto._OrigineDemandeJF;

@Feminin
public class OrigineDemandeJF extends _OrigineDemandeJF {
	private static final long serialVersionUID = -7092605111745828505L;

	@Override
	public String toString() {
		return getLibelle();
	}
}



