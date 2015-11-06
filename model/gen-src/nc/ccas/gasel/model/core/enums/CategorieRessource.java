package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.core.enums.auto._CategorieRessource;
import nc.ccas.gasel.modelUtils.EnumerationSync;

@Feminin
public class CategorieRessource extends _CategorieRessource implements
		Enumeration {
	private static final long serialVersionUID = -3490325722380746406L;

	public static final int TRAVAIL = 1;

	static {
		EnumerationSync sync = new EnumerationSync(CategorieRessource.class);
		sync.add(TRAVAIL, "Ressources du travail");
	}

	@Override
	public String toString() {
		return getLibelle();
	}

}
