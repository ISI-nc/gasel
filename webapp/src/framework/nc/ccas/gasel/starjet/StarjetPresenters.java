package nc.ccas.gasel.starjet;

import static nc.ccas.gasel.starjet.aides.TypeCourrierAide.COURRIER_ADMINISTRE;
import static nc.ccas.gasel.starjet.aides.TypeCourrierAide.FAX_FOURNISSEUR;

import java.util.HashMap;
import java.util.Map;

import nc.ccas.gasel.starjet.aides.CourrierAideEau;
import nc.ccas.gasel.starjet.aides.CourrierAideOM;
import nc.ccas.gasel.starjet.aides.EditionBons;

public class StarjetPresenters {

	private static Map<String, AbstractStarjetPresenter<?>> presenters = new HashMap<String, AbstractStarjetPresenter<?>>();

	public static AbstractStarjetPresenter<?> get(String view) {
		return presenters.get(view);
	}

	public static void register(String view, AbstractStarjetPresenter<?> pres) {
		if (presenters.containsKey(view)) {
			throw new RuntimeException("Duplicate: " + view);
		}
		presenters.put(view, pres);
	}

	static {
		register("bon", new EditionBons());

		register("aide.om.admin", new CourrierAideOM(COURRIER_ADMINISTRE));
		register("aide.om.fax", new CourrierAideOM(FAX_FOURNISSEUR));

		register("aide.eau.admin", new CourrierAideEau(COURRIER_ADMINISTRE,
				false));
		register("aide.eau.admin.partielle", new CourrierAideEau(
				COURRIER_ADMINISTRE, true));
		register("aide.eau.fax", new CourrierAideEau(FAX_FOURNISSEUR));
		register("aide.gaz.fax", new CourrierAideOM(FAX_FOURNISSEUR));
	}

}
