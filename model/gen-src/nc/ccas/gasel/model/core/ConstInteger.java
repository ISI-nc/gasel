package nc.ccas.gasel.model.core;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.core.auto._ConstInteger;

@Feminin
public class ConstInteger extends _ConstInteger {
	private static final long serialVersionUID = 1093268737403751722L;
	
	public ConstInteger() {
		setValeur(0);
	}

	@Override
	public String toString() {
		return getLibelle();
	}

}



