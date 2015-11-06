package nc.ccas.gasel.model.aides;

import org.apache.cayenne.DataObjectUtils;

import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.aides.auto._TypeArrete;
import nc.ccas.gasel.modelUtils.EnumerationSync;

public class TypeArrete extends _TypeArrete implements Enumeration {
	private static final long serialVersionUID = -7840449541295841318L;

	public static final int EAU = 1;
	public static final int OM = 2;
	public static final int AVANCE = 3;
	public static final int SOLIDARITE = 4;
	public static final int ALIMENTATION = 5;
	public static final int EEC = 6;
	public static final int URGENCES = 7;

	static {
		EnumerationSync sync = new EnumerationSync(TypeArrete.class);
		sync.add(EAU, "Calédonienne des Eaux");
		sync.add(OM, "Ordures ménagères");
		sync.add(AVANCE, "Avance");
		sync.add(SOLIDARITE, "Solidarité");
		sync.add(ALIMENTATION, "Alimentation");
		sync.add(EEC, "EEC");
		sync.add(URGENCES, "Urgences");
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

	public boolean isAvance() {
		return getId() == AVANCE;
	}

	public boolean isEau() {
		return getId() == EAU;
	}

	public boolean isOM() {
		return getId() == OM;
	}

	public boolean isSolidarite() {
		return getId() == SOLIDARITE;
	}

	public boolean isAlimentation() {
		return getId() == ALIMENTATION;
	}

	public boolean isEec() {
		return getId() == EEC;
	}

	public boolean isUrgences() {
		return getId() == URGENCES;
	}

}
