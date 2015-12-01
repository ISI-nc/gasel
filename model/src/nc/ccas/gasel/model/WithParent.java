package nc.ccas.gasel.model;

import org.apache.cayenne.Persistent;

public interface WithParent {

	public Persistent getParent();

}
