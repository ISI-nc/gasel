package nc.ccas.gasel;

import static nc.ccas.gasel.Utils.ensureDataContext;

import java.io.Serializable;

import org.apache.cayenne.DataObject;
import org.apache.tapestry.IRequestCycle;

public abstract class ObjectCallback<P extends BasePage, T> implements
		Serializable {
	private static final long serialVersionUID = 3810023882256285219L;

	public abstract void performCallback(P page, T o);

	private final String pageName;

	public ObjectCallback(P page) {
		this(page.getPageName());
	}

	public ObjectCallback(String pageName) {
		this.pageName = pageName;
	}

	@SuppressWarnings("unchecked")
	public void performCallback(IRequestCycle cycle, T o) {
		P p = (P) cycle.getPage(pageName);
		T target;
		if (o instanceof DataObject) {
			target = (T) ensureDataContext((DataObject) o, p.getObjectContext());
		} else {
			target = o;
		}

		performCallback(p, target);
	}

}
