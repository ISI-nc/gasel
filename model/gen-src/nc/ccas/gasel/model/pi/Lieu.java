package nc.ccas.gasel.model.pi;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.pi.auto._Lieu;

public class Lieu extends _Lieu implements ComplexDeletion {
	private static final long serialVersionUID = 1492250575495999438L;

	public void prepareForDeletion() {
		DeletionUtils.empty(this, JARDINS_PROPERTY);
	}

}
