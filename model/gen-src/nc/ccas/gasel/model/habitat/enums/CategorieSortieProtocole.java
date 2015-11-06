package nc.ccas.gasel.model.habitat.enums;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.habitat.enums.auto._CategorieSortieProtocole;

@Feminin
public class CategorieSortieProtocole extends _CategorieSortieProtocole {

	private static final long serialVersionUID = 3057062330258034151L;
	
	@Override
	public String toString() {
		return getLibelle();
	}
	
}



