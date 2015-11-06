package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._SituationFamiliale;

@Feminin
public class SituationFamiliale extends _SituationFamiliale implements
		Enumeration {
	private static final long serialVersionUID = 555157874836868506L;

	@Override
	public String toString() {
		return getLibelle();
	}

}
