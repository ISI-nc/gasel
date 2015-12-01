package nc.ccas.gasel.pages.aides;

import java.util.ArrayList;
import java.util.List;

import nc.ccas.gasel.docs.aides.AideEauParams;
import nc.ccas.gasel.docs.aides.AideOMParams;
import nc.ccas.gasel.docs.aides.AideParams;
import nc.ccas.gasel.pages.DocumentsPage;
import nc.ccas.gasel.pages.pi.RefDescVarsDocument;

public abstract class DocumentsAides extends DocumentsPage {
	
	public static void main(String[] args) {
		printTableCode(DESCS);
	}

	private static final List<RefDescVarsDocument> DESCS = new ArrayList<RefDescVarsDocument>();
	static {
		DESCS.add(new RefDescVarsDocument("aides.courrier_eau", "Courrier eau",
				AideEauParams.PROVIDED));
		DESCS.add(new RefDescVarsDocument("aides.courrier_eau_partiel", "Courrier eau (PeC partielle)",
				AideEauParams.PROVIDED));
		DESCS.add(new RefDescVarsDocument("aides.courrier_frn_eau", "Courrier eau (fournisseur)",
				AideEauParams.PROVIDED));
		DESCS.add(new RefDescVarsDocument("aides.courrier_om", "Courrier OM",
				AideOMParams.PROVIDED));
		DESCS.add(new RefDescVarsDocument("aides.courrier_frn_om", "Courrier OM (fournisseur)",
				AideOMParams.PROVIDED));
		DESCS.add(new RefDescVarsDocument("aides.courrier_frn_gaz", "Courrier gaz (fournisseur)",
				AideParams.PROVIDED));
	}
	
	@Override
	public List<RefDescVarsDocument> getDescs() {
		return DESCS;
	}

}
