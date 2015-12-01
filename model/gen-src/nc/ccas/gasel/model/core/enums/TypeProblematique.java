package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._TypeProblematique;

public class TypeProblematique extends _TypeProblematique implements
		Enumeration {

	private static final long serialVersionUID = 5501334551332991313L;

	@Override
	public String toString() {
		return getLibelle();
	}

}
