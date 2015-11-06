package nc.ccas.gasel.model.habitat.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.habitat.enums.auto._Institution;

@Feminin
public class Institution extends _Institution {

	private static final long serialVersionUID = 3321873631633145613L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



