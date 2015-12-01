package nc.ccas.gasel.model.vues;

import java.util.ArrayList;
import java.util.List;

import nc.ccas.gasel.SqlParams;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.apache.hivemind.util.Defense;

public abstract class HasAideMontantsHelper implements HasAideMontants {

	protected abstract String expression();

	protected abstract void fillParameters(SqlParams params);

	private List<AideResumeMontants> _values;

	private final Persistent parent;

	public HasAideMontantsHelper(Persistent parent) {
		Defense.notNull(parent, "parent");
		this.parent = parent;
	}

	@SuppressWarnings("unchecked")
	public List<AideResumeMontants> getAideMontants() {
		if (_values == null) {
			SelectQuery query = new SelectQuery(AideResumeMontants.class,
					buildExpression());
			query.addOrdering("db:annee_mois", true);
			query.addOrdering("aide.dossier.dossier.chefFamille.nom", true);
			query.addOrdering("aide.dossier.dossier.chefFamille.prenom", true);
			query.addOrdering("db:id", true);
			_values = context().performQuery(query);
		}
		return _values;
	}

	private Expression buildExpression() {
		Expression expr = Expression.fromString(expression());
		SqlParams params = new SqlParams();
		fillParameters(params);
		expr = expr.expWithParameters(params);
		return expr;
	}

	public void clear() {
		_values = null;
	}

	public List<AideResumeMontants> filter(List<AideResumeMontants> montants) {
		Expression expr = buildExpression();
		ArrayList<AideResumeMontants> retval = new ArrayList<AideResumeMontants>(
				montants.size());
		for (AideResumeMontants montant : montants) {
			if (!(Boolean) expr.evaluate(montant))
				continue;
			retval.add(montant);
		}
		retval.trimToSize();
		return retval;
	}

	private ObjectContext context() {
		return parent.getObjectContext();
	}

}
