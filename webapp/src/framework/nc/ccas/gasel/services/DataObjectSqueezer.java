package nc.ccas.gasel.services;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.ObjectId;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.ObjEntity;
import org.apache.commons.codec.binary.Base64;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;

public class DataObjectSqueezer implements SqueezeAdaptor {

	private static Set<String> pkFields(ObjEntity entity) {
		Set<String> pkFields = new TreeSet<String>();
		for (Object o : entity.getDbEntity().getPrimaryKey()) {
			DbAttribute attr = (DbAttribute) o;
			pkFields.add(attr.getName());
		}
		return pkFields;
	}

	public Class<?> getDataClass() {
		return DataObject.class;
	}

	public String getPrefix() {
		return "Qq";
	}

	public String squeeze(DataSqueezer squeezer, Object data) {
		DataObject obj = (DataObject) data;
		ObjectContext context = obj.getObjectContext();
		if (context == null) {
			throw new RuntimeException("Objet sans contexte: "
					+ obj.getObjectId() + " " + obj);
		}
		ObjEntity entity = context.getEntityResolver().lookupObjEntity(obj);

		if (obj.getObjectId().isTemporary()) {
			byte[] key = obj.getObjectId().getKey();
			return "q" + entity.getName() + ":"
					+ new String(Base64.encodeBase64(key));
		}

		// Serialize ObjectId
		StringBuilder pk = new StringBuilder();
		ObjectId oid = obj.getObjectId();
		for (String field : pkFields(entity)) {
			if (pk.length() > 0)
				pk.append('|');
			pk.append(squeezer.squeeze(oid.getIdSnapshot().get(field)));
		}

		return "Q" + entity.getName() + ":" + pk;
	}

	public Object unsqueeze(DataSqueezer squeezer, String string) {
		ObjectContext context = DataContext.getThreadDataContext();

		String[] split = string.split(":", 2);
		String entityName = split[0].substring(1);
		String[] keyPart = split[1].split("\\|");

		if (string.startsWith("Q")) {
			ObjEntity entity = context.getEntityResolver().getObjEntity(
					entityName);
			Map<String, Object> pk = new TreeMap<String, Object>();
			int i = 0;
			for (String field : pkFields(entity)) {
				pk.put(field, squeezer.unsqueeze(keyPart[i++]));
			}
			return DataObjectUtils.objectForPK(context, entityName, pk);
		} else { // startsWith("q") => objet temporaire
			byte[] key = Base64.decodeBase64(keyPart[0].getBytes());
			ObjectId oid = new ObjectId(entityName, key);
			return ((DataContext) context).getObjectStore().getNode(oid);
		}
	}

}
