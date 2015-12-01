package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._CategorieCharge;

@Feminin
public class CategorieCharge extends _CategorieCharge implements Enumeration {
	private static final long serialVersionUID = -1176943634738227703L;

	@Override
	public String toString() {
		return getLibelle();
	}

}



