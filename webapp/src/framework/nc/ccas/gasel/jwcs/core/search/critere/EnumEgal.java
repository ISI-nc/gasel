package nc.ccas.gasel.jwcs.core.search.critere;

import java.util.List;

import nc.ccas.gasel.jwcs.core.edit.Enum;
import nc.ccas.gasel.jwcs.core.search.Critere;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.Persist;

import com.asystan.common.cayenne_new.QueryFactory;

public abstract class EnumEgal extends Critere {

	@Parameter
	public abstract String getEnum();

	@Parameter
	public abstract List<? extends DataObject> getValues();

	@Component(type = "core/edit/Enum", bindings = { "value=value",
			"values=values", "enum=enum" }, inheritInformalParameters = true)
	public abstract Enum getSelect();

	@Persist("workflow")
	public abstract DataObject getValue();

	@Override
	protected void renderImpl(IMarkupWriter writer, IRequestCycle cycle) {
		getSelect().render(writer, cycle);
	}

	@Override
	public Expression buildExpressionImpl(String path) {
		if (getValue() == null) {
			return null;
		}
		return QueryFactory.createEquals(path, getValue());
	}

}
