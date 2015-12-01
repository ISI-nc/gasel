package nc.ccas.gasel.model.pe;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.pe.auto._FormationAM;

@Feminin
public class FormationAM extends _FormationAM {

	private static final long serialVersionUID = 312612638169098409L;

	@Override
	public String toString() {
		return getFormation().getLibelle();
	}

}
