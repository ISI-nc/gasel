package nc.ccas.gasel.model.habitat.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.habitat.enums.auto._CategorieSocialeLogement;

@Feminin
public class CategorieSocialeLogement extends _CategorieSocialeLogement {

	private static final long serialVersionUID = -7343745255689231684L;
	
	@Override
	public String toString() {
		return getLibelle();
	}

}



