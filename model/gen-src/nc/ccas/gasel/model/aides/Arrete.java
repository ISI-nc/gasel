package nc.ccas.gasel.model.aides;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.aides.auto._Arrete;

public class Arrete extends _Arrete implements ComplexDeletion {
	private static final long serialVersionUID = -3640041319087094461L;

	public Arrete() {
		setEdite(false);
	}

	public void prepareForDeletion() {
		DeletionUtils.empty(this, BONS_VALIDES_PROPERTY);
	}
	
	public Integer getMontant() {
		int sum = 0;
		for (UsageBon u : getBonsValides()) {
			sum += u.getMontantUtilise();
		}
		return sum;
	}

}
