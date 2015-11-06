import java.text.DateFormat;
import java.text.SimpleDateFormat;

import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.access.DataContext;


public class Temp5 {

	private static final DateFormat FORMAT_DATE = new SimpleDateFormat(
			"dd/MM/yyyy");

	public static void main(String[] args) throws Exception {

		DataContext oc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(oc);

		for (Bon bon : CommonQueries.select(Bon.class, "numero = '201007005001'")) {
			System.out.println(bon.getNumero());
		}
		
		//oc.commitChanges();
	}

}
