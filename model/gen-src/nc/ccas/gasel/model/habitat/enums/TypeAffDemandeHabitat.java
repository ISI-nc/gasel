package nc.ccas.gasel.model.habitat.enums;

import nc.ccas.gasel.model.habitat.enums.auto._TypeAffDemandeHabitat;
import nc.ccas.gasel.modelUtils.EnumerationSync;

import org.apache.cayenne.DataObjectUtils;

public class TypeAffDemandeHabitat extends _TypeAffDemandeHabitat {

	private static final long serialVersionUID = 7931272495901284081L;

	public static final int LOCATIF = 1;
	
	public static final int REHABILITATION = 2;

	public static final int ACCESSION = 3;

	static {
		EnumerationSync sync = new EnumerationSync(TypeAffDemandeHabitat.class);
		sync.add(LOCATIF, "Locatif");
		sync.add(REHABILITATION, "Réhabilitation");
		sync.add(ACCESSION, "Accession");
	}

	@Override
	public String toString() {
		return getLibelle();
	}
	
	public boolean isLocatif() {
		return getId() == LOCATIF;
	}
	
	public boolean isRehabilitation() {
		return getId() == REHABILITATION;
	}
	
	public boolean isAccession() {
		return getId() == ACCESSION;
	}


	public Integer getId() {
		return (Integer) DataObjectUtils.pkForObject(this);
	}
	
}
