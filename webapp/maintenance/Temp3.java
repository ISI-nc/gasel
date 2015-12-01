import java.util.Calendar;
import java.util.List;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;


public class Temp3 {

	public static void main(String[] args) {

		DataContext oc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(oc);

		BasePageSql sql = new BasePageSql(oc);

		for (int i = Calendar.JANUARY; i <= Calendar.DECEMBER; i++) {
			List<Bon> bons = sql.query(Bon.class, Expression.fromString(
					"debut between $debut and $fin").expWithParameters(
					sql.params() //
							.timestamp("debut", DateUtils.debutMois(2010, i)) //
							.timestamp("fin", DateUtils.finMois(2010, i))),
					"aide");

			int countP = 0;
			int countO = 0;
			int countU = 0;
			for (Bon bon : bons) {
				if (bon.getAide().getStatut().isOccasionnelle()) {
					countO++;
				} else if (bon.getAide().getStatut().isPlurimensuelle()) {
					countP++;
				} else if (bon.getAide().getStatut().isImmediate()) {
					countU++;
				}
			}

			System.out.println((i + 1) + "\t" + bons.size() + "\t" + countP
					+ "\t" + countO + "\t" + countU);
		}
	}
}
