package nc.ccas.gasel.jwcs.core.search.critere;

import nc.ccas.gasel.jwcs.core.search.Critere;

import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.PropertySelection;
import org.apache.tapestry.form.TextField;

import com.asystan.common.cayenne.QueryFactory;

public abstract class Contient extends Critere {

	private static final int MATCH_CONTIENT = 0;

	private static final int MATCH_COMMENCE_PAR = 1;

	private static final int MATCH_FINI_PAR = 2;

	@Parameter(defaultValue = "true")
	public abstract boolean getIgnoreCase();

	@Parameter
	public abstract String getOnly();

	@Persist("workflow")
	public abstract String getValue();

	@Persist("workflow")
	@InitialValue("0")
	public abstract int getTypeMatch();

	@Component(type = "PropertySelection", bindings = { "value=ognl:typeMatch",
			"model=ognl:typeMatchModel" })
	public abstract PropertySelection getTypeMatchField();

	@Component(type = "TextField", bindings = { "value=ognl:value" })
	public abstract TextField getField();

	protected void renderImpl(IMarkupWriter writer, IRequestCycle cycle) {
		if (getOnly() == null) {
			getTypeMatchField().render(writer, cycle);
		} else {
			writer.print(getOnly());
			writer.print(" ");
		}
		getField().render(writer, cycle);
	};

	public Expression buildExpressionImpl(String path) {
		String value = getValue();
		if (value == null) {
			return null;
		}
		int typeMatch;
		if (getOnly() == null) {
			typeMatch = getTypeMatch();
		} else {
			String only = getOnly();
			// XXX Moche
			if ("commence par".equals(only)) {
				typeMatch = MATCH_COMMENCE_PAR;
			} else if ("fini par".equals(only)) {
				typeMatch = MATCH_FINI_PAR;
			} else if ("contient".equals(only)) {
				typeMatch = MATCH_CONTIENT;
			} else {
				throw new IllegalArgumentException("only == " + only);
			}
		}
		switch (typeMatch) {
		case MATCH_CONTIENT:
			if (getIgnoreCase()) {
				return QueryFactory.createLikeIgnoreCase(path, "%" + value
						+ "%");
			} else {
				return QueryFactory.createLike(path, "%" + value + "%");
			}
		case MATCH_COMMENCE_PAR:
			if (getIgnoreCase()) {
				return QueryFactory.createLikeIgnoreCase(path, value + "%");
			} else {
				return QueryFactory.createLike(path, value + "%");
			}
		case MATCH_FINI_PAR:
			if (getIgnoreCase()) {
				return QueryFactory.createLikeIgnoreCase(path, "%" + value);
			} else {
				return QueryFactory.createLike(path, "%" + value);
			}
		default:
			throw new RuntimeException("typeMatch invalide : " + getTypeMatch());
		}
	}

	public IPropertySelectionModel getTypeMatchModel() {
		return TYPE_MATCH_MODEL;
	}

	private static final IPropertySelectionModel TYPE_MATCH_MODEL = new IPropertySelectionModel() {
		public String getLabel(int index) {
			switch (index) {
			case MATCH_CONTIENT:
				return "contient";
			case MATCH_COMMENCE_PAR:
				return "commence par";
			case MATCH_FINI_PAR:
				return "fini par";
			default:
				throw new RuntimeException("Index invalide : " + index);
			}
		}

		public Object getOption(int index) {
			return index;
		}

		public int getOptionCount() {
			return 3;
		}

		public String getValue(int index) {
			return Integer.toString(index);
		}

		public Object translateValue(String value) {
			return Integer.parseInt(value);
		}

		public boolean isDisabled(int index) {
			return false;
		}

	};

}
