package nc.ccas.gasel.jwcs.core.search.critere;

import static com.asystan.common.cayenne_new.QueryFactory.createTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.jwcs.core.edit.PackedSubSet;
import nc.ccas.gasel.jwcs.core.edit.subset.CollectionSSLH;
import nc.ccas.gasel.jwcs.core.edit.subset.SubSetListHandler;
import nc.ccas.gasel.jwcs.core.search.Critere;
import nc.ccas.gasel.modelUtils.SqlUtils;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

public abstract class Enum extends Critere implements PageBeginRenderListener {

	@SuppressWarnings("unchecked")
	private static final List<? extends DataObject> getAll(String clazz,
			DataContext context) {
		SelectQuery query = new SelectQuery(clazz, createTrue("actif"));
		query.addOrdering(new Ordering("libelle", true));
		return context.performQuery(query);
	}

	@Parameter(required = true)
	public abstract String getEnum();

	@Parameter(defaultValue = "3", name = "cols")
	public abstract int getColumns();

	@Component(type = "core/edit/PackedSubSet", bindings = { "handler=handler",
			"elements=enumValues", "cols=columns" })
	public abstract PackedSubSet<? extends DataObject> getPackedSubSet();

	public List<? extends DataObject> getEnumValues() {
		return getAll(getEnum(), dataContext());
	}

	@Override
	protected void renderImpl(IMarkupWriter writer, IRequestCycle cycle) {
		getPackedSubSet().render(writer, cycle);
	}

	@Override
	public Expression buildExpressionImpl(String path) {
		if (getSelectedValues().isEmpty()) {
			return null;
		}
		return SqlUtils.createIn(path, getSelectedValues());
	}

	public abstract DataObject getIter();

	public abstract void setIter(DataObject iter);

	public Integer getIterId() {
		if (getIter() == null) {
			return null;
		}
		return DataObjectUtils.intPKForObject(getIter());
	}

	public void setIterId(int id) {
		setIter((DataObject) DataObjectUtils.objectForPK(dataContext(), getEnum(), id));
	}

	private DataContext dataContext() {
		return ((BasePage) getPage()).getObjectContext();
	}

	@Persist("workflow")
	public abstract SubSetListHandler<DataObject> getHandler();

	public abstract void setHandler(SubSetListHandler<DataObject> values);

	public Collection<DataObject> getSelectedValues() {
		List<DataObject> retval = new LinkedList<DataObject>();
		for (DataObject o : getHandler())
			retval.add(o);
		return retval;
	}

	public void pageBeginRender(PageEvent event) {
		if (getHandler() == null) {
			setHandler(new CollectionSSLH<DataObject>(new HashSet<DataObject>()));
		}
	}

}
