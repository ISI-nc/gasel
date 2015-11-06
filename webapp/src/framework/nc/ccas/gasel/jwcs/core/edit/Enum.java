package nc.ccas.gasel.jwcs.core.edit;

import static com.asystan.common.cayenne.QueryFactory.createTrue;
import static nc.ccas.gasel.modelUtils.CayenneUtils.resolveEntity;

import java.util.List;

import nc.ccas.gasel.psm.EnumPSM;
import nc.ccas.gasel.workflow.Workflow;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.query.SelectQuery;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.PropertySelection;

public abstract class Enum extends AbstractComponent {

	@Parameter
	public abstract String getEnum();

	@Parameter
	public abstract List<? extends DataObject> getValues();

	@Parameter(required = true)
	public abstract Object getValue();

	@Parameter(defaultValue = "false")
	public abstract boolean getDisabled();

	@Parameter(defaultValue = "literal:--")
	public abstract String getNull();

	private IPropertySelectionModel _model;

	@SuppressWarnings("unchecked")
	public IPropertySelectionModel getModel() {
		if (_model == null) {
			List<? extends DataObject> values;
			Class<?> valueClass;
			if (getValues() != null) {
				values = getValues();
				valueClass = values.isEmpty() ? null : values.get(0).getClass();
			} else {
				ObjectContext dc = getObjectContext();
				ObjEntity entity = resolveEntity(getEnum());
				if (entity == null) {
					throw new ApplicationRuntimeException(
							"Pas d'énumeration nommée " + getEnum());
				}
				SelectQuery query = new SelectQuery(entity, createTrue("actif"));
				query.addOrdering("libelle", true);
				values = dc.performQuery(query);
				valueClass = entity.getJavaClass();
			}
			_model = new EnumPSM(getNull(), values, getObjectContext(),
					valueClass);
		}
		return _model;
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_model = null;
	}

	@InjectState("workflow")
	public abstract Workflow getWorkflow();

	@Component(type = "PropertySelection", bindings = { "value=value",
			"model=model", "disabled=disabled" }, inheritInformalParameters = true)
	public abstract PropertySelection getSelect();

	public ObjectContext getObjectContext() {
		try {
			return DataContext.getThreadDataContext();
		} catch (IllegalStateException ise) {
			return getWorkflow().getCurrentEntry().getObjectContext();
		}
	}

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		getSelect().render(writer, cycle);
	}

}
