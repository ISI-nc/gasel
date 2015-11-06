import nc.ccas.gasel.model.paph.AccompagnementPAPH;
import nc.ccas.gasel.model.paph.HandicapPAPH;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import org.apache.cayenne.access.DataContext;


public class Temp6 {

	public static void main(String[] args) throws Exception {

		DataContext oc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(oc);

		//Dossier input = CommonQueries.getAll(Dossier.class).get(0);
		//Dossier input = CommonQueries.findById(Dossier.class, 19940002);
		//System.out.println(input.getId());
		
		HandicapPAPH input = new HandicapPAPH();
		
		StringBuilder buf = new StringBuilder();
		for (AccompagnementPAPH acc : input.getDossier().getAccompagnements()) {
			if (buf.length() > 0) buf.append("; ");
			buf.append(acc.getProjet());
		}
		
		System.out.println(buf.toString());
	}
}
