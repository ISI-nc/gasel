package nc.ccas.gasel.jwcs.core.search.critere;

import nc.ccas.gasel.jwcs.core.search.Critere;

import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Persist;

import com.asystan.common.cayenne.QueryFactory;

public abstract class Date extends Critere {

	@Persist("workflow")
	public abstract java.util.Date getDate();

	public abstract void setDate(java.util.Date date);

	@Component(type = "core/edit/Date", bindings = { "value=date" })
	public abstract IComponent getDateSelect();

	@Override
	protected void renderImpl(IMarkupWriter writer, IRequestCycle cycle) {
		getDateSelect().render(writer, cycle);
	}

	@Override
	protected Expression buildExpressionImpl(String path) {
		if (getDate() == null)
			return null;
		return QueryFactory.createEquals(path, getDate());
	}

}
