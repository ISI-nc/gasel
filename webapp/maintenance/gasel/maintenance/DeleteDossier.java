package gasel.maintenance;

import org.apache.cayenne.access.DataContext;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;

public class DeleteDossier {
	
	public static void main(String[] args) {
		if (true) { // secu
			System.exit(0);
		}
		

		DataContext dc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(dc);
		
		Dossier dossier = CommonQueries.findById(Dossier.class, 201301150);
		dc.deleteObject(dossier.getChefFamille());
		dc.deleteObject(dossier);
		
		dc.commitChanges();
	}

}
