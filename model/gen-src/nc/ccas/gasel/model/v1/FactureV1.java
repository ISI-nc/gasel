package nc.ccas.gasel.model.v1;

import nc.ccas.gasel.model.v1.auto._FactureV1;

public class FactureV1 extends _FactureV1 {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5370963969870086113L;

	public Integer getMontantBons() {
		int sum = 0;
		for (BonV1 bon : getTBonArray()) {
			sum += bon.getMontantUtilise().intValue();
		}
		return sum;
	}

}



