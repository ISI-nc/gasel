package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._SituationProfessionelle;

@Feminin
public class SituationProfessionelle extends _SituationProfessionelle implements
		Enumeration {

	private static final long serialVersionUID = -2124261358076619533L;

	@Override
	public String toString() {
		return getLibelle();
	}

}
