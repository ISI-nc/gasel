package nc.ccas.gasel.jwcs.core.search.critere;

import nc.ccas.gasel.jwcs.core.search.Critere;

import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.TextField;
import org.apache.tapestry.form.translator.Translator;

import com.asystan.common.cayenne_new.QueryFactory;

public abstract class Egal extends Critere {

	@Persist("workflow")
	public abstract Object getValue();

	@Parameter(defaultValue = "translator:string")
	public abstract Translator getTranslator();
	
	@Component(type = "TextField", bindings = { "value=ognl:value", "translator=translator" })
	public abstract TextField getField();

	protected void renderImpl(IMarkupWriter writer, IRequestCycle cycle) {
		getField().render(writer, cycle);
	};

	public Expression buildExpressionImpl(String path) {
		Object value = getValue();
		if (value == null || (value instanceof Number && ((Number) value).intValue() == 0)) {
			return null;
		}
		return QueryFactory.createEquals(path, value);
	}

}
