package nc.ccas.gasel.model.habitat.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.habitat.enums.auto._NatureAideLogement;

@Feminin
public class NatureAideLogement extends _NatureAideLogement {

	private static final long serialVersionUID = -661899390646709613L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



