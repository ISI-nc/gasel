package test.aides;

import static org.apache.cayenne.DataObjectUtils.intPKForObject;

import java.util.List;

import junit.framework.TestCase;
import nc.ccas.gasel.agents.aides.BonsNoel;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.access.DataContext;


public class TestBonsNoel extends TestCase {

	static {
	}

	public void testGeneration() throws Exception {
		DataContext.bindThreadDataContext(CayenneUtils.createDataContext());

		List<Aide> aides = BonsNoel.INSTANCE.createAidesNoel();

		System.out.println(aides.size() + " aides générées");
		System.out.println();
		for (Aide aide : aides) {
			Dossier dossier = aide.getDossier().getDossier();
			System.out.println( //
					intPKForObject(dossier) + "\t" //
							+ dossier.getChefFamille().getNom() + "\t" //
							+ dossier.getChefFamille().getPrenom() + "\t" //
							+ aide.getMontant() + "\t" //
							+ aide.getNature() + "\t" //
							+ aide.getStatut() + "\t" //
					);
		}
	}

}
