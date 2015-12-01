package nc.ccas.gasel.jwcs.core.edit.subset;

import java.util.Iterator;
import java.util.List;

import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;

import com.asystan.common.beans.BeanUtils;
import com.asystan.common.lists.ListUtils;

public class JoinListSSLH<T extends DataObject> implements SubSetListHandler<T> {

	private final DataObject source;

	private final String toMany;

	private final String toOne;

	private Class<?> joinObjectClass;

	public JoinListSSLH(DataObject source, String toMany, String toOne) {
		this.source = source;
		this.toMany = toMany;
		ObjRelationship toManyRel = CayenneUtils.relation(source, this.toMany);
		joinObjectClass = ((ObjEntity) toManyRel.getTargetEntity())
				.getJavaClass();
		this.toOne = toOne;
	}

	public boolean contains(T value) {
		for (T remote : this) {
			if (BeanUtils.nullSafeEquals(value, remote)) {
				return true;
			}
		}
		return false;
	}

	public void add(T value) {
		if (contains(value))
			return;
		source.addToManyTarget(toMany, createLinkTo(value), true);
	}

	public void remove(T value) {
		for (DataObject link : links()) {
			if (BeanUtils.nullSafeEquals(value, link.readProperty(toOne))) {
				source.removeToManyTarget(toMany, link, true);
				link.getObjectContext().deleteObject(link);
			}
		}
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private final Iterator<? extends DataObject> linkIt = links()
					.iterator();

			public boolean hasNext() {
				return linkIt.hasNext();
			}

			@SuppressWarnings("unchecked")
			public T next() {
				return (T) linkIt.next().readProperty(toOne);
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	private List<? extends DataObject> links() {
		return ListUtils.cast(DataObject.class, (List<?>) source.readProperty(toMany));
	}

	private DataObject createLinkTo(T value) {
		ObjectContext dc = DataContext.getThreadDataContext();
		DataObject link = (DataObject) dc.newObject(joinObjectClass);
		link.writeProperty(toOne, value);
		return link;
	}

}
