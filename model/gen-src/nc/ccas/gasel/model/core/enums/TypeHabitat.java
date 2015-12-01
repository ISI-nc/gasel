package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._TypeHabitat;

import org.apache.cayenne.DataObjectUtils;

public class TypeHabitat extends _TypeHabitat implements Enumeration {
	private static final long serialVersionUID = -1720901778082628662L;

	public Integer getId() {
		return DataObjectUtils.intPKForObject(this);
	}
	
	@Override
	public String toString() {
		return getLibelle();
	}

}
