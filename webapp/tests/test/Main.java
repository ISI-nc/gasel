package test;

import static java.util.Collections.singletonMap;
import static org.apache.cayenne.access.DataContext.getThreadDataContext;
import static org.apache.cayenne.exp.Expression.fromString;
import nc.ccas.gasel.model.DeletionUtils;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.pages.dossiers.Suppression;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.exp.Expression;


public class Main {

	public static void main(String[] args) {
		Expression expr = fromString("modifDate < $limite").expWithParameters(
				singletonMap("limite", Suppression.getLimite()));
		for (Dossier dossier : CommonQueries.select(Dossier.class, expr)) {

			System.out.println("- Dossier n°"
					+ DataObjectUtils.intPKForObject(dossier) + " (" + dossier
					+ ")");

			DeletionUtils.delete(dossier);
			getThreadDataContext().commitChanges();
		}
	}
}
