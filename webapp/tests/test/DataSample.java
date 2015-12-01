package test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.modelUtils.CayenneUtils;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectId;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.map.ObjAttribute;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;

public class DataSample {

	private final DataContext context = CayenneUtils.createDataContext();

	private final Map<String, DataObject> objects = new HashMap<String, DataObject>();

	private int lastId = 1;

	public <T extends DataObject> T create(Class<T> clazz) {
		return create(clazz, null, null);
	}

	public <T extends DataObject> T create(Class<T> clazz, String ref) {
		return create(clazz, ref, null);
	}

	public <T extends DataObject> T create(Class<T> clazz, int id) {
		return create(clazz, null, id);
	}

	public <T extends DataObject> T create(Class<T> clazz, String ref,
			Integer id) {
		T object = clazz.cast(context.newObject(clazz));

		if (ref != null) {
			objects.put(ref, object);
		}

		if (id == null)
			id = lastId++; // ID par défaut

		String entityName = object.getObjectId().getEntityName();
		Map<String, Integer> idMap = Collections.singletonMap("id", id);
		object.setObjectId(new ObjectId(entityName, idMap));

		return object;
	}

	public <T extends DataObject> void copy(T template, T destination) {
		ObjEntity ent = template.getObjectContext().getEntityResolver()
				.lookupObjEntity(template);

		// Attributes
		for (Object o : ent.getAttributes()) {
			ObjAttribute attribute = (ObjAttribute) o;
			String propertyName = attribute.getName();
			Object value = template.readProperty(propertyName);
			destination.writePropertyDirectly(propertyName, value);
		}

		// Relationships
		for (Object o : ent.getRelationships()) {
			ObjRelationship rel = (ObjRelationship) o;
			String propertyName = rel.getName();
			if (rel.isToMany()) {
				// ToMany
				Collection<?> sourceList = (Collection<?>) template
						.readProperty(propertyName);
				List<DataObject> list = new ArrayList<DataObject>(sourceList
						.size());
				for (Object target : sourceList) {
					list.add((DataObject) target);
				}
				destination.writePropertyDirectly(propertyName, list);
			} else {
				// ToOne
				Object value = template.readProperty(propertyName);
				destination.writePropertyDirectly(propertyName, value);
			}
		}
	}
}
