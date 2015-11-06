package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._LienParente;
import nc.ccas.gasel.modelUtils.EnumerationSync;

import org.apache.cayenne.DataObjectUtils;

public class LienParente extends _LienParente implements Enumeration {
	private static final long serialVersionUID = 4172314181871253674L;

	// EACF = Enfant A Charge Fiscalement

	public static final int EACF_COUPLE = 1;
	public static final int EACF_CF = 2;
	public static final int EACF_CONJOINT = 3;
	public static final int EACF_ADOPTE = 4;
	public static final int EACF_CONFIE = 5;

	static {
		EnumerationSync sync = new EnumerationSync(LienParente.class);

		sync.add(EACF_COUPLE, "Enfant du couple à charge fiscalement");
		sync.add(EACF_CF, "Enfant du chef de famille à charge fiscalement");
		sync.add(EACF_CONJOINT, "Enfant du conjoint à charge fiscalement");
		sync.add(EACF_ADOPTE, "Enfant adopté à charge fiscalement");
		sync.add(EACF_CONFIE, "Enfant confié à charge fiscalement");
	}

	@Override
	public String toString() {
		return getLibelle();
	}

	public Integer getId() {
		if (getObjectId().isTemporary())
			return null;
		return DataObjectUtils.intPKForObject(this);
	}

	public boolean isEnfantAChargeFiscalement() {
		switch (getId()) {
		case EACF_COUPLE:
		case EACF_CF:
		case EACF_CONJOINT:
		case EACF_ADOPTE:
		case EACF_CONFIE:
			return true;
		default:
			return false;
		}
	}

}
