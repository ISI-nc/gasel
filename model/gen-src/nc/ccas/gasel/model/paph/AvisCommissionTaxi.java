package nc.ccas.gasel.model.paph;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.paph.auto._AvisCommissionTaxi;

public class AvisCommissionTaxi extends _AvisCommissionTaxi implements
		ComplexDeletion {

	private static final long serialVersionUID = 1981071682254894611L;

	public void prepareForDeletion() {
		DeletionUtils.delete(getDemandesTaxi());
	}

}
