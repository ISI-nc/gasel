package tests.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.map.ObjEntity;

public class EntityTestCase extends TestCase {

	private static ObjEntity entity(String name) {
		return Configuration.getSharedConfiguration().getDomain()
				.getEntityResolver().getObjEntity(name);
	}

	public static final Collection<ObjEntity> entities() {
		List<ObjEntity> retval = new ArrayList<ObjEntity>();
		retval.addAll(Configuration.getSharedConfiguration().getDomain()
				.getEntityResolver().getObjEntities());
		Collections.sort(retval, new Comparator<ObjEntity>() {
			@Override
			public int compare(ObjEntity o1, ObjEntity o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return retval;
	}

	protected final ObjEntity entity;

	public EntityTestCase(String entityName) {
		super(entityName);
		this.entity = entity(entityName);
	}

}
