package nc.ccas.gasel.model.aides;

import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.aides.auto._TypeArticle;

public class TypeArticle extends _TypeArticle implements Enumeration {
	private static final long serialVersionUID = 7405587775628765060L;

	@Override
	public String toString() {
		return getLibelle();
	}

}



