package nc.ccas.gasel.pages;

import java.util.Collection;
import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.modelUtils.EnumerationSync;

import org.apache.cayenne.map.ObjEntity;
import org.apache.tapestry.event.PageEvent;

public abstract class Deploy extends BasePage {

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		for (ObjEntity ent : (Collection<ObjEntity>) getObjectContext()
				.getEntityResolver().getObjEntities()) {
			ent.getJavaClass();
		}
	}

	public List<Class<?>> getSyncableClasses() {
		return sort(EnumerationSync.syncableClasses()).by("name").results();
	}

	public void enumSync(String className) throws ClassNotFoundException {
		EnumerationSync.sync(Class.forName(className));
	}

	public abstract Class<?> getClazz();

	public boolean getSynced() {
		return EnumerationSync.synced(getClazz());
	}

}
