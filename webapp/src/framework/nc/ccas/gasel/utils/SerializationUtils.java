package nc.ccas.gasel.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.Persistent;

public class SerializationUtils {
	
	// Objets

	public static void storeId(ObjectOutputStream out, Persistent persistent)
			throws IOException {
		if (persistent.getObjectId().isTemporary())
			throw new IllegalArgumentException("Not persisted");
		out.writeInt(DataObjectUtils.intPKForObject(persistent));
	}

	public static <T extends Persistent> T restoreId(ObjectInputStream in,
			Class<T> clazz) throws IOException {
		return CommonQueries.findById(clazz, in.readInt());
	}
	
	// Listes

	public static void storeList(ObjectOutputStream out,
			Collection<? extends Persistent> collection) throws IOException {
		out.writeInt(collection.size());
		for (Persistent p : collection) {
			storeId(out, p);
		}
	}

	public static <T extends Persistent> List<T> restoreList(
			ObjectInputStream in, Class<T> clazz) throws IOException {
		int size = in.readInt();
		List<T> retval = new ArrayList<T>(size);
		restoreList(in, clazz, size, retval);
		return retval;
	}

	public static <T extends Persistent> void restoreList(ObjectInputStream in,
			Class<T> clazz, Collection<T> collection) throws IOException {
		int size = in.readInt();
		collection.clear();
		restoreList(in, clazz, size, collection);
	}

	private static <T extends Persistent> void restoreList(
			ObjectInputStream in, Class<T> clazz, int size,
			Collection<T> collection) throws IOException {
		for (int i = 0; i < size; i++) {
			collection.add(restoreId(in, clazz));
		}
	}

}
