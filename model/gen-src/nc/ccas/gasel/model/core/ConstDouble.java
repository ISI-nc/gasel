package nc.ccas.gasel.model.core;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.core.auto._ConstDouble;

@Feminin
public class ConstDouble extends _ConstDouble {

	private static final long serialVersionUID = 7528057639879923953L;
	
	public ConstDouble() {
		setValeur(0.0);
	}

	@Override
	public String toString() {
		return getLibelle();
	}

}
