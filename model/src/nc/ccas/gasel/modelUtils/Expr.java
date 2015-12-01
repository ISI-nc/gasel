package nc.ccas.gasel.modelUtils;

import static com.asystan.common.cayenne_new.QueryFactory.createAnd;
import static com.asystan.common.cayenne_new.QueryFactory.createIsNull;
import static com.asystan.common.cayenne_new.QueryFactory.createOr;
import static nc.ccas.gasel.modelUtils.SqlUtils.dateToExpr;
import static org.apache.cayenne.exp.Expression.fromString;

import java.util.Date;

import org.apache.cayenne.exp.Expression;

public class Expr {

	public static Expression intersectionPeriode(String propDebut, Date debut,
			String propFin, Date fin) {
		return createAnd( //
				nullOr(propDebut, "<= " + dateToExpr(fin)), //
				nullOr(propFin, ">= " + dateToExpr(debut)));
	}

	public static Expression intersectionPeriode(Date debut, Date fin) {
		return intersectionPeriode("debut", debut, "fin", fin);
	}

	private static Expression nullOr(String prop, String expression) {
		return createOr(createIsNull(prop), fromString(prop + " " + expression));
	}

}
