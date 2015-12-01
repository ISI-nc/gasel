package nc.ccas.gasel.pages.pi;

import java.util.ArrayList;
import java.util.List;

import nc.ccas.gasel.docs.pi.ArreteJFParams;
import nc.ccas.gasel.pages.DocumentsPage;

public abstract class DocumentsPI extends DocumentsPage {
	
	public static void main(String[] args) {
		printTableCode(DESCS);
	}

	private static final List<RefDescVarsDocument> DESCS = new ArrayList<RefDescVarsDocument>();
	static {
		DESCS.add(new RefDescVarsDocument("pi.arrete_attribution",
				"Arrêté d'attribution", ArreteJFParams.PROVIDED));
		DESCS.add(new RefDescVarsDocument("pi.arrete_renouvellement",
				"Arrêté de renouvellement", ArreteJFParams.PROVIDED));
		DESCS.add(new RefDescVarsDocument("pi.retard_paiement",
				"Retard de paiement", ArreteJFParams.PROVIDED));
		DESCS.add(new RefDescVarsDocument("pi.entretien",
				"Entretien insuffisant", ArreteJFParams.PROVIDED));
		DESCS.add(new RefDescVarsDocument("pi.entretien_paiement",
				"Paiement+entretien", ArreteJFParams.PROVIDED));
	}

	public List<RefDescVarsDocument> getDescs() {
		return DESCS;
	}

}
