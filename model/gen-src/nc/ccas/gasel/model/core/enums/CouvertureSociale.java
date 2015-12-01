package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._CouvertureSociale;

@Feminin
public class CouvertureSociale extends _CouvertureSociale implements Enumeration {
	private static final long serialVersionUID = 3798543876959999082L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



