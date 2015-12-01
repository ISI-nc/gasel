package nc.ccas.gasel.model.import_v1;

import nc.ccas.gasel.model.import_v1.auto._ImportV1;

public class ImportV1 extends _ImportV1 {
	private static final long serialVersionUID = 236559246052351878L;

	@Override
	public String toString() {
		return "Import from " + getSrc() + " to " + getDst();
	}

	public String getSrc() {
		return getSrcEntity() + "[" + getSrcId() + "]";
	}

	public String getDst() {
		return getDstEntity() + "[" + getDstId() + "]";
	}

}
