package nc.ccas.gasel.model.pe.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.pe.enums.auto._AutorisationParentale;

@Feminin
public class AutorisationParentale extends _AutorisationParentale {

	private static final long serialVersionUID = 3950517830649395499L;

	@Override
	public String toString() {
		return getLibelle();
	}

}