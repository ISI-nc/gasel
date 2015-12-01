package nc.ccas.gasel.model.aides;

import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.aides.auto._MotifRefusAide;

public class MotifRefusAide extends _MotifRefusAide implements Enumeration {

	private static final long serialVersionUID = 5255324457530268853L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



