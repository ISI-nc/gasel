package nc.ccas.gasel.model;

import static java.util.Collections.unmodifiableSet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * Not thread safe.
 * 
 * @author ISI.NC - Mikaël Cluseau
 * 
 * @param <T>
 *            The type of node.
 */
public class Automate<T> {

	private T initialState;

	private Map<T, Set<T>> transitions = new HashMap<T, Set<T>>();

	public Automate() {
		initialize();
	}

	/**
	 * Method called by the constructor.
	 */
	protected void initialize() {
		// skip
	}

	/*
	 * Building
	 */

	public void add(T src, @SuppressWarnings("unchecked") T... dsts) {
		transitionsFrom(src).addAll(Arrays.asList(dsts));
	}

	public void remove(T src, @SuppressWarnings("unchecked") T... dsts) {
		Set<T> transitions = transitionsFrom(src);
		for (T to : dsts) {
			transitions.remove(to);
		}
	}

	public void setInitialState(T initialState) {
		this.initialState = initialState;
	}

	/*
	 * Checking
	 */

	public boolean hasTransition(T from, T to) {
		return transitionsFrom(from).contains(to);
	}

	public void checkTransition(T from, T to) {
		if (!hasTransition(from, to)) {
			throw new IllegalStateException("Illegal state change: " + from
					+ " -> " + to);
		}
	}

	/*
	 * Accessing
	 */

	public Set<T> getTransitionsFrom(T from) {
		return unmodifiableSet(transitionsFrom(from));
	}

	public Set<T> getNodes() {
		HashSet<T> nodes = new HashSet<T>();
		for (Map.Entry<T, Set<T>> entry : transitions.entrySet()) {
			nodes.add(entry.getKey());
			nodes.addAll(entry.getValue());
		}
		return nodes;
	}

	private Set<T> transitionsFrom(T from) {
		Set<T> set = transitions.get(from);
		if (set == null) {
			set = new HashSet<T>();
			transitions.put(from, set);
		}
		return set;
	}

	public T getInitialState() {
		return initialState;
	}

	/*
	 * DOT output
	 */

	public String toDot() {
		StringWriter buf = new StringWriter();
		try {
			saveToDot(buf);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return buf.toString();
	}

	public void saveToDot(String fileName) throws IOException {
		FileWriter file = new FileWriter(fileName);
		try {
			saveToDot(file);
		} finally {
			file.close();
		}
	}

	public void saveToDot(Writer buf) throws IOException {
		buf.append("digraph \"\" {\n");

		int nodeId = 0;
		Map<T, Integer> refs = new HashMap<T, Integer>();
		for (T node : getNodes()) {
			nodeId++;
			buf.append("\tn" + nodeId + " [label=\""
					+ node.toString().replace("\"", "\\\"") + "\"];\n");
			refs.put(node, nodeId);
		}

		for (Map.Entry<T, Set<T>> entry : transitions.entrySet()) {
			buf.append("\n");
			Integer fromId = refs.get(entry.getKey());
			for (T to : entry.getValue()) {
				Integer toId = refs.get(to);
				buf.append("\tn" + fromId + " -> n" + toId + ";\n");
			}
		}
		buf.append("}\n");
	}

}
