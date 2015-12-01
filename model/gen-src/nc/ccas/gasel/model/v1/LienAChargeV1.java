package nc.ccas.gasel.model.v1;

import nc.ccas.gasel.model.v1.auto._LienAChargeV1;

public class LienAChargeV1 extends _LienAChargeV1 {

	private static final long serialVersionUID = -5404601009364499266L;

	@Override
	public String toString() {
		EnumerationV1 enumeration = getLienDeParente().getEnumeration();
		return enumeration.getKey() + ": " + enumeration.getLabel();
	}

}
