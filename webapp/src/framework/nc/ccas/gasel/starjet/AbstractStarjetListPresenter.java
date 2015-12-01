package nc.ccas.gasel.starjet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import com.asystan.common.lists.ListUtils;

@SuppressWarnings("rawtypes")
public abstract class AbstractStarjetListPresenter<T> extends
		AbstractStarjetPresenter<Collection> {

	private final Class<T> elementClass;

	protected abstract void writeImplImpl(PrintWriter out, List<T> value)
			throws IOException;

	public AbstractStarjetListPresenter(Class<T> clazz) {
		super(Collection.class);
		elementClass = clazz;
	}

	@Override
	protected final void writeImpl(PrintWriter out, Collection collection)
			throws IOException {
		writeImplImpl(out, ListUtils.cast(elementClass, collection));
	}

}