package gasel.maintenance;

import nc.ccas.gasel.beans.Fusion;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.services.impl.FusionneurDossier;

import org.apache.cayenne.access.DataContext;

public class FusionDossier {
	
	public static void main(String[] args) {
		DataContext dc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(dc);
		
		Dossier main = CommonQueries.findById(Dossier.class, 20130067);
		Dossier sec = CommonQueries.findById(Dossier.class, 20100096);
		
		Fusion<Dossier> fusion = new Fusion<>();
		fusion.setElementPrincipal(main);
		fusion.setElementSecondaire(sec);
		
		new FusionneurDossier().fusionner(fusion);
		
		dc.commitChanges();
	}
	
}
