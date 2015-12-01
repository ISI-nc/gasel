package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._ReponseProblematique;

@Feminin
public class ReponseProblematique extends _ReponseProblematique implements Enumeration {

	private static final long serialVersionUID = -6742324943040723385L;

	@Override
	public String toString() {
		return getLibelle();
	}

}
