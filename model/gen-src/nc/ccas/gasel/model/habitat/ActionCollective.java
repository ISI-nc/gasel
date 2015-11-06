package nc.ccas.gasel.model.habitat;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.habitat.auto._ActionCollective;

@Feminin
public class ActionCollective extends _ActionCollective implements
		ComplexDeletion {

	private static final long serialVersionUID = -1284798056407703875L;

	public void prepareForDeletion() {
		DeletionUtils.empty(this, DOSSIERS_PROPERTY);
	}

}
