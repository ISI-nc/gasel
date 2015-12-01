package nc.ccas.gasel.model.paph.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.paph.enums.auto._SituationSociale;

@Feminin
public class SituationSociale extends _SituationSociale {

	private static final long serialVersionUID = 4181110844062318094L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



