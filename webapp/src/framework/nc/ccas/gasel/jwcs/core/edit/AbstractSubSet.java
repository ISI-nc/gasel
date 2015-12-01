package nc.ccas.gasel.jwcs.core.edit;

import java.util.List;

import nc.ccas.gasel.jwcs.core.edit.subset.CollectionSSLH;
import nc.ccas.gasel.jwcs.core.edit.subset.JoinListSSLH;
import nc.ccas.gasel.jwcs.core.edit.subset.SubSetListHandler;
import nc.ccas.gasel.jwcs.core.edit.subset.ToManySSLH;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.AbstractFormComponent;

public abstract class AbstractSubSet<T> extends AbstractFormComponent implements
		SubSetParameters<T> {

	protected static final String PATH_FORMAT = "[A-Za-z0-9_]+\\.[A-Za-z0-9_]+";

	private SubSetListHandler<T> _handler;

	// ------------------------------------------------------------------------
	// Rendu
	//

	protected abstract void renderCellContents(IMarkupWriter writer,
			IRequestCycle cycle);

	@Override
	protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		renderCommon(writer, cycle, getElements());
	}

	@Override
	protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		renderCommon(writer, cycle, getElements());
	}

	@SuppressWarnings("unchecked")
	protected final void renderCommon(IMarkupWriter writer,
			IRequestCycle cycle, List<T> cellValues) {
		int cols = getColumnCount();
		int rows = (int) Math.ceil( //
				((double) cellValues.size()) / ((double) cols));
		// impossible: T[rows][cols]... dommage
		Object[][] table = new Object[rows][cols];
		for (int col = 0; col < cols; col++) {
			for (int row = 0; row < rows; row++) {
				int pos = col * rows + row;
				if (pos >= cellValues.size()) {
					continue;
				}
				table[row][col] = cellValues.get(pos);
			}
		}
		writer.begin("table");
		writer.appendAttribute("class", "subset invis");
		for (Object[] row : table) {
			writer.begin("tr");
			for (Object obj : row) {
				if (obj == null) {
					continue;
				}
				setValue((T) obj);
				writer.begin("td");
				renderCellContents(writer, cycle);
				writer.end();
			}
			writer.end();
		}
		writer.end();
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns the handler.
	 */
	protected SubSetListHandler<T> handler() {
		if (_handler == null) {
			buildHandler();
		}
		return _handler;
	}

	private void buildHandler() {
		int methodCount = (getHandler() != null ? 1 : 0)
				+ (getValuesParam() != null ? 1 : 0)
				+ (getSourceObject() != null ? 1 : 0);
		if (methodCount == 0) {
			throw new IllegalArgumentException("You must supply a value "
					+ "for handler, values or from.");
		} else if (methodCount > 1) {
			throw new IllegalArgumentException(
					"Parameters handler, values and from are mutualy exclusive.");
		}

		if (getHandler() != null) {
			_handler = getHandler();
		} else if (getValuesParam() != null) {
			_handler = new CollectionSSLH<T>(getValuesParam());
		} else if (getSourceObject() != null) {
			_handler = buildHandlerForSourceObject();
		} else {
			throw new RuntimeException("unreachable");
		}
	}

	@SuppressWarnings("unchecked")
	private SubSetListHandler<T> buildHandlerForSourceObject() {
		// Here, <T> must extends DataObject
		if (getList() != null && getPathFromSource() != null) {
			throw new IllegalArgumentException(
					"Parameters list and path are mutualy exclusive.");
		} else if (getList() != null) {
			return new ToManySSLH(getSourceObject(), getList());
		} else if (getPathFromSource() != null) {
			if (!getPathFromSource().matches(PATH_FORMAT)) {
				throw new IllegalArgumentException(
						"Invalid format for path. Must be toMany.toOne");
			}
			String[] pathElements = getPathFromSource().split("\\.");
			return new JoinListSSLH(getSourceObject(), pathElements[0],
					pathElements[1]);
		} else {
			throw new IllegalArgumentException(
					"When using from, a list or path parameter is required.");
		}
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_handler = null;
		_value = null;
	}

	// ------------------------------------------------------------------------

	private T _value;

	public T getValue() {
		return _value;
	}

	public void setValue(T value) {
		if (getBinding("value") != null) {
			getBinding("value").setObject(value);
		}
		_value = value;
	}

}
