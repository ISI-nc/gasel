package nc.ccas.gasel.model.v1;

import nc.ccas.gasel.model.v1.auto._BonV1;

public class BonV1 extends _BonV1 {
	private static final long serialVersionUID = -5094068138803333801L;

	public Integer getMontantUtilise() {
		Double montant = getMontant();
		if (montant == null)
			return null;

		Double montantNonUtilise = getMontantNonUtilise();
		if (montantNonUtilise == null)
			return montant.intValue();

		return getMontant().intValue() - getMontantNonUtilise().intValue();
	}

}
