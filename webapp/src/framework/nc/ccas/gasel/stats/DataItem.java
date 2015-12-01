package nc.ccas.gasel.stats;

import java.util.LinkedList;
import java.util.List;

public class DataItem {

	private final List<Object>[] data;

	@SuppressWarnings("unchecked")
	public DataItem(int numCols) {
		data = new List[numCols];
		for (int i = 0; i < numCols; i++)
			data[i] = new LinkedList<Object>();
	}

	public List<Object> get(int i) {
		return data[i];
	}

	public void add(int i, Object data) {
		this.data[i].add(data);
	}

}
