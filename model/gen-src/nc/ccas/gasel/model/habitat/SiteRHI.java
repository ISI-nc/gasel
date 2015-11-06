package nc.ccas.gasel.model.habitat;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.habitat.auto._SiteRHI;

public class SiteRHI extends _SiteRHI implements ComplexDeletion {

	private static final long serialVersionUID = -8145817876974955782L;

	@Override
	public String toString() {
		return getNom();
	}

	public void prepareForDeletion() {
		DeletionUtils.delete(getObservations());
	}

}
