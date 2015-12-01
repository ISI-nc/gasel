package nc.ccas.gasel.model.core;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.core.auto._ConstString;

@Feminin
public class ConstString extends _ConstString {
	private static final long serialVersionUID = -1383115212275434049L;
	
	public ConstString() {
		setValeur("");
	}

	@Override
	public String toString() {
		return getLibelle();
	}

}



