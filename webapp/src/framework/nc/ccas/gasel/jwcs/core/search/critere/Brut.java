package nc.ccas.gasel.jwcs.core.search.critere;

import nc.ccas.gasel.jwcs.core.search.Critere;

import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.parser.ASTDbPath;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;

public abstract class Brut extends Critere {

	@Parameter(required = true)
	public abstract String getSql();

	@Override
	protected void renderImpl(IMarkupWriter writer, IRequestCycle cycle) {
		// skip
	}

	@Override
	protected Expression buildExpressionImpl(String path) {
		if (isDisabled())
			return null;

		return new RawSqlExpr(new ASTDbPath(path), getSql());
	}

}
