package nc.ccas.gasel.model.pe.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.pe.enums.auto._Formation;

@Feminin
public class Formation extends _Formation {

	private static final long serialVersionUID = 3442009588310488948L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



