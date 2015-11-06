package nc.ccas.gasel.model.habitat.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.habitat.enums.auto._NatureAideSociale;

@Feminin
public class NatureAideSociale extends _NatureAideSociale {

	private static final long serialVersionUID = 4250448192760823334L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



