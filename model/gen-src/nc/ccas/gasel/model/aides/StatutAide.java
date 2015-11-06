package nc.ccas.gasel.model.aides;

import org.apache.cayenne.DataObjectUtils;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.aides.auto._StatutAide;
import nc.ccas.gasel.modelUtils.EnumerationSync;

@Feminin
public class StatutAide extends _StatutAide implements Enumeration {
	private static final long serialVersionUID = -6256883875730696099L;

	public static final int IMMEDIATE = 1;

	public static final int PLURIMENSUELLE = 2;

	public static final int OCCASIONNELLE = 3;

	static {
		EnumerationSync sync = new EnumerationSync(StatutAide.class);
		sync.add(IMMEDIATE, "Immédiate");
		sync.add(PLURIMENSUELLE, "Plurimensuelle");
		sync.add(OCCASIONNELLE, "Occasionnelle");
	}

	public Integer getId() {
		return (Integer) DataObjectUtils.pkForObject(this);
	}

	@Override
	public String toString() {
		return getLibelle();
	}

	public boolean isImmediate() {
		return getId() == IMMEDIATE;
	}

	public boolean isPlurimensuelle() {
		return getId() == PLURIMENSUELLE;
	}

	public boolean isOccasionnelle() {
		return getId() == OCCASIONNELLE;
	}

}
