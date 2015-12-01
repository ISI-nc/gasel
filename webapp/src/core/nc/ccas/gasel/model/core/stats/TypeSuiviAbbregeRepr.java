package nc.ccas.gasel.model.core.stats;

import java.util.Map;
import java.util.TreeMap;

import nc.ccas.gasel.model.aides.AspectAides;
import nc.ccas.gasel.model.habitat.AspectDossierHabitat;
import nc.ccas.gasel.model.paph.AspectDossierPAPH;
import nc.ccas.gasel.model.pe.AspectDossierAM;
import nc.ccas.gasel.model.pe.AspectDossierEnfant;
import nc.ccas.gasel.model.pi.AspectDossierPI;
import nc.ccas.gasel.stats.repr.TranslateRepr;

public class TypeSuiviAbbregeRepr extends TranslateRepr {

	private static final Map<String, String> ABBR = new TreeMap<String, String>();
	static {
		ABBR.put(AspectAides.class.getSimpleName(), "AS");
		ABBR.put(AspectDossierEnfant.class.getSimpleName(), "PE");
		ABBR.put(AspectDossierAM.class.getSimpleName(), "AM");
		ABBR.put(AspectDossierPAPH.class.getSimpleName(), "PA/PH");
		ABBR.put(AspectDossierPI.class.getSimpleName(), "PI");
		ABBR.put(AspectDossierHabitat.class.getSimpleName(), "Lgt");
	}

	// Attention, doit être après la partie statique !
	public static final TypeSuiviAbbregeRepr INSTANCE = new TypeSuiviAbbregeRepr();

	private TypeSuiviAbbregeRepr() {
		super(ABBR);
	}

}
