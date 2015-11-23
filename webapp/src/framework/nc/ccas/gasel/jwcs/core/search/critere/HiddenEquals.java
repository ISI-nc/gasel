package nc.ccas.gasel.jwcs.core.search.critere;

import nc.ccas.gasel.jwcs.core.search.Critere;

import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;

import com.asystan.common.cayenne_new.QueryFactory;

public abstract class HiddenEquals extends Critere {

	@Parameter(required = true)
	public abstract Object getValue();

	@Override
	protected void renderImpl(IMarkupWriter writer, IRequestCycle cycle) {
		// skip
	}

	@Override
	public Expression buildExpressionImpl(String path) {
		return QueryFactory.createEquals(path, getValue());
	}
	
	@Override
	public boolean isHidden() {
		return true;
	}

}
